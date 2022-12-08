package pl.sg.application.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.service.ApplicationUserService;

import java.io.IOException;
import java.util.Optional;

import static pl.sg.twofa.QRCode.fromApplicationUser;

@RestController
@RequestMapping("/register")
@Slf4j
@Validated
public class RegistrationController {

    public static final String APP_NAME = "accountant";
    private final ApplicationUserService applicationUserService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(ApplicationUserService applicationUserService,
                                  PasswordEncoder passwordEncoder) {
        this.applicationUserService = applicationUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public String registerUser(@RequestBody @Valid User user) {
        String uname = user.getName();
        String upass = user.getPass();
        if (uname == null || upass == null || "".equals(uname.trim()) || "".equals(upass.trim())) {
            throw new BadCredentialsException("Username/ password is required");
        }
        Optional<ApplicationUser> firstByLogin = applicationUserService.findByUserLogins(uname);
        if (firstByLogin.isPresent()) {
            throw new BadCredentialsException(String.format("Username %s already exists.", uname));
        }
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setLogin(uname);
        applicationUser.setPassword(passwordEncoder.encode(upass));
        applicationUserService.create(applicationUser);
        return fromApplicationUser(APP_NAME, uname, applicationUser.getSecret()).qrLink();
    }

    @PostMapping("/setup2FA")
    public String setup2FA(@RequestBody @Valid User user) {
        ApplicationUser applicationUser = applicationUserService.getByUserLogins(user.getName());
        if (!passwordEncoder.matches(user.getPass(), applicationUser.getPassword())) {
            throw new BadCredentialsException("Wrong user/password");
        }
        if (applicationUser.isUsing2FA()) {
            throw new BadCredentialsException("2FA already configured");
        }
        Totp totp = new Totp(applicationUser.getSecret());
        if (!totp.verify(String.valueOf(user.getSecretFor2FA()))) {
            throw new BadCredentialsException("Invalid verification code");
        }
        applicationUser.setUsing2FA(true);
        applicationUserService.save(applicationUser);
        return "It's good";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestBody @Valid ChangePasswordUser user) {
        ApplicationUser applicationUser = applicationUserService.getByUserLogins(user.getName());
        if (!passwordEncoder.matches(user.getOldpass(), applicationUser.getPassword())) {
            throw new BadCredentialsException("Wrong user/password");
        }
        if (!applicationUser.isUsing2FA()) {
            throw new BadCredentialsException("2FA already configured");
        }
        Totp totp = new Totp(applicationUser.getSecret());
        if (!totp.verify(String.valueOf(user.getAuthcode()))) {
            throw new BadCredentialsException("User not registered properly");
        }
        applicationUser.setPassword(passwordEncoder.encode(user.getNewpass()));
        applicationUserService.save(applicationUser);
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

    public static class ChangePasswordUser {
        @NotBlank
        private String name;
        @NotBlank
        private String oldpass;
        @NotBlank
        private String authcode;
        @NotBlank
        private String newpass;

        public ChangePasswordUser() {
        }

        public ChangePasswordUser(@NotBlank String name, @NotBlank String oldpass, @NotBlank String authcode, @NotBlank String newpass) {
            this.name = name;
            this.oldpass = oldpass;
            this.authcode = authcode;
            this.newpass = newpass;
        }

        public String getName() {
            return name;
        }

        public String getOldpass() {
            return oldpass;
        }

        public String getAuthcode() {
            return authcode;
        }

        public String getNewpass() {
            return newpass;
        }
    }
}
