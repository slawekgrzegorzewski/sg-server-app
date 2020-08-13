package pl.sg.application.security;

import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ApplicationUserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@RestController
@RequestMapping("/login")
@Slf4j
@CrossOrigin
public class LoginController {

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;

    @Autowired
    public LoginController(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder, AuthorizationService authorizationService) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public ResponseEntity<String> login(@RequestBody User user, @RequestHeader("x-tfa") String token) {
        String uname = user.getName();
        String upass = user.getPass();
        if (uname == null || upass == null || "".equals(uname.trim()) || "".equals(upass.trim())) {
            throw new BadCredentialsException("user/password is required");
        }
        ApplicationUser firstByLogin = applicationUserRepository.findFirstByLogin(uname).orElseThrow(
                () -> new BadCredentialsException("Wrong user/password"));
        if (!passwordEncoder.matches(upass, firstByLogin.getPassword())) {
            throw new BadCredentialsException("Wrong user/password");
        }
        if (!new Totp(firstByLogin.getSecret()).verify(token)) {
            throw new BadCredentialsException("Wrong 2FA code");
        }
        String jwt = authorizationService.generateJWTToken(uname, firstByLogin.getRoles());
        return ResponseEntity.ok().body(HEADER_STRING + ": " + TOKEN_PREFIX + " " + jwt);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestHeader("Authorization") String bearerToken) {
        authorizationService.validate(bearerToken);
        return ResponseEntity.ok().body("OK");
    }

    @ExceptionHandler({BadCredentialsException.class})
    public void handleException(HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(400);
        response.getWriter().write(ex.getMessage());
        response.getWriter().flush();
        response.getWriter().close();
    }

    public static class User {
        private String name;
        private String pass;

        public User() {
        }

        public User(String name, String pass) {
            this.name = name;
            this.pass = pass;
        }

        public String getName() {
            return name;
        }

        public String getPass() {
            return pass;
        }
    }
}
