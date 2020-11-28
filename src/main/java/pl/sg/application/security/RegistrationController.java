package pl.sg.application.security;

import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ApplicationUserLogin;
import pl.sg.application.model.ApplicationUserLoginRepository;
import pl.sg.application.model.ApplicationUserRepository;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static pl.sg.twofa.QRCode.fromApplicationUser;

@RestController
@RequestMapping("/register")
@Slf4j
@Validated
public class RegistrationController {

    public static final String APP_NAME = "accountant";
    private final ApplicationUserRepository applicationUserRepository;
    private final ApplicationUserLoginRepository applicationUserLoginRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;

    @Autowired
    public RegistrationController(ApplicationUserRepository applicationUserRepository,
                                  ApplicationUserLoginRepository applicationUserLoginRepository,
                                  PasswordEncoder passwordEncoder,
                                  AuthorizationService authorizationService) {
        this.applicationUserRepository = applicationUserRepository;
        this.applicationUserLoginRepository = applicationUserLoginRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public String registerUser(@RequestBody @Valid User user) {
        String uname = user.getName();
        String upass = user.getPass();
        if (uname == null || upass == null || "".equals(uname.trim()) || "".equals(upass.trim())) {
            throw new BadCredentialsException("Username/ password is required");
        }
        Optional<ApplicationUser> firstByLogin = applicationUserRepository.findFirstByUserLogins(uname);
        if (firstByLogin.isPresent()) {
            throw new BadCredentialsException(String.format("Username %s already exists.", uname));
        }
        ApplicationUser applicationUser = new ApplicationUser();
        ApplicationUserLogin applicationUserLogin = new ApplicationUserLogin();
        applicationUserLogin.setLogin(uname);
        applicationUserLogin.setPassword(passwordEncoder.encode(upass));
        applicationUser.setUserLogins(List.of(applicationUserLogin));
        applicationUser.setLoggedInUser(applicationUserLogin);
        applicationUserLogin.setApplicationUser(applicationUser);
        applicationUserRepository.save(applicationUser);
        applicationUserLoginRepository.save(applicationUserLogin);
        return fromApplicationUser(APP_NAME, uname, applicationUser.getLoggedInUser().getSecret()).qrLink();
    }

    @PostMapping("/setup2FA")
    public String setup2FA(@RequestBody @Valid User user) {
        ApplicationUser applicationUser = applicationUserRepository.findFirstByUserLogins(user.getName()).orElseThrow(() -> new RuntimeException("Wrong user/password"));
        authorizationService.setLoggedInUser(applicationUser, user.getName());
        ApplicationUserLogin firstByLogin = applicationUser.getLoggedInUser();
        if (!passwordEncoder.matches(user.getPass(), firstByLogin.getPassword())) {
            throw new BadCredentialsException("Wrong user/password");
        }
        if (firstByLogin.isUsing2FA()) {
            throw new BadCredentialsException("2FA already configured");
        }
        Totp totp = new Totp(firstByLogin.getSecret());
        if (!totp.verify(String.valueOf(user.getSecretFor2FA()))) {
            throw new BadCredentialsException("Invalid verification code");
        }
        firstByLogin.setUsing2FA(true);
        applicationUserRepository.save(applicationUser);
        return "It's good";
    }

    @ExceptionHandler({BadCredentialsException.class})
    public void handleException(HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(400);
        response.getWriter().write(ex.getMessage());
        response.getWriter().flush();
        response.getWriter().close();
    }

    public static class User {
        @NotBlank
        private String name;
        @NotBlank
        private String pass;
        private Integer secretFor2FA;

        public User() {
        }

        public User(String name, String pass, Integer secretFor2FA) {
            this.name = name;
            this.pass = pass;
            this.secretFor2FA = secretFor2FA;
        }

        public String getName() {
            return name;
        }

        public String getPass() {
            return pass;
        }

        public Integer getSecretFor2FA() {
            return secretFor2FA;
        }
    }
}
