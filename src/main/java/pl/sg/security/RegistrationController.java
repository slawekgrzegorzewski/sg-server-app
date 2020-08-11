package pl.sg.security;

import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.Totp;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
@Slf4j
public class RegistrationController {

    public static final String APP_NAME = "accountant";
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

    @Autowired
    public RegistrationController(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        String uname = user.getUname();
        String upass = user.getUpass();
        Integer token = user.getToken();
        if (token == null) {
            return registerUser(uname, upass);
        } else {
            return confirmToken(uname, upass, token);
        }
    }

    @NotNull
    private ResponseEntity<String> registerUser(String uname, String upass) {
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
        applicationUser.setUsing2FA(true);
        applicationUserRepository.save(applicationUser);
        return ResponseEntity.ok().body(generateQRUrl(applicationUser));
    }

    private ResponseEntity<String> confirmToken(String uname, String upass, Integer token) {

        ApplicationUser firstByLogin = applicationUserRepository.findFirstByLogin(uname).orElseThrow(() -> new RuntimeException("Wrong user/password"));
        if (!passwordEncoder.matches(upass, firstByLogin.getPassword())) {
            throw new BadCredentialsException("Wrong user/password");
        }
        Totp totp = new Totp(firstByLogin.getSecret());
        if (!totp.verify(String.valueOf(token))) {
            throw new BadCredentialsException("Invalid verfication code");
        }

        return ResponseEntity.ok().body("It's good");
    }

    private String generateQRUrl(ApplicationUser applicationUser) {
        return QR_PREFIX + String.format("otpauth://totp/%s?secret=%s&issuer=%s",
                APP_NAME,
                applicationUser.getSecret(),
                APP_NAME);
    }

    @ExceptionHandler({ BadCredentialsException.class })
    public void handleException(HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(400);
        response.getWriter().write(ex.getMessage());
        response.getWriter().flush();
        response.getWriter().close();
    }

    public static class User {
        private String uname;
        private String upass;
        private Integer token;

        public User() {
        }

        public User(String uname, String upass, Integer token) {
            this.uname = uname;
            this.upass = upass;
            this.token = token;
        }

        public String getUname() {
            return uname;
        }

        public String getUpass() {
            return upass;
        }

        public Integer getToken() {
            return token;
        }
    }
}
