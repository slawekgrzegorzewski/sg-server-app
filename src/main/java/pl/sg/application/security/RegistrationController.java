package pl.sg.application.security;

import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ApplicationUserRepository;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.Optional;

import static pl.sg.twofa.QRCode.fromApplicationUser;

@RestController
@RequestMapping("/register")
@Slf4j
@Validated
public class RegistrationController {

    public static final String APP_NAME = "accountant";
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public String registerUser(@RequestBody @Valid User user) {
        String uname = user.getName();
        String upass = user.getPass();
        if (uname == null || upass == null || "".equals(uname.trim()) || "".equals(upass.trim())) {
            throw new BadCredentialsException("Username/ password is required");
        }
        Optional<ApplicationUser> firstByLogin = applicationUserRepository.findFirstByLogin(uname);
        if (firstByLogin.isPresent()) {
            throw new BadCredentialsException(String.format("Username %s already exists.", uname));
        }
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setLogin(uname);
        applicationUser.setPassword(passwordEncoder.encode(upass));
        applicationUserRepository.save(applicationUser);
        return fromApplicationUser(APP_NAME, applicationUser).qrLink();
    }

    @PostMapping("/setup2FA")
    public String setup2FA(@RequestBody @Valid User user) {
        ApplicationUser firstByLogin = applicationUserRepository.findFirstByLogin(user.getName()).orElseThrow(() -> new RuntimeException("Wrong user/password"));
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
        applicationUserRepository.save(firstByLogin);
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
