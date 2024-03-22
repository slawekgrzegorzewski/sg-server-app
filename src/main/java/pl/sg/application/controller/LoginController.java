package pl.sg.application.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.application.service.ApplicationUserService;
import pl.sg.application.service.AuthorizationService;

import java.io.IOException;

@RestController
@RequestMapping("/login")
@Slf4j
@CrossOrigin
@Validated
@Profile({"https", "http"})
public class LoginController {

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";
    private final ApplicationUserService applicationUserService;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;

    @Autowired
    public LoginController(ApplicationUserService applicationUserService, PasswordEncoder passwordEncoder, AuthorizationService authorizationService) {
        this.applicationUserService = applicationUserService;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public String login(@RequestBody @Valid User user, @RequestHeader("x-tfa") String token) {
        String uname = user.getName();
        String upass = user.getPass();
        if (uname == null || upass == null || uname.trim().isEmpty() || upass.trim().isEmpty()) {
            throw new BadCredentialsException("user/password is required");
        }

        ApplicationUser firstByLogin = applicationUserService.getByUserLogins(uname);

        if (!passwordEncoder.matches(upass, firstByLogin.getPassword())) {
            throw new BadCredentialsException("Wrong user/password");
        }
        if (!new Totp(firstByLogin.getSecret()).verify(token)) {
            throw new BadCredentialsException("Wrong 2FA code");
        }
        String jwt = authorizationService.generateJWTToken(
                firstByLogin.getId() + ":" + uname, firstByLogin.getRoles(),
                firstByLogin.getDefaultDomain().getId());
        return HEADER_STRING + ": " + TOKEN_PREFIX + " " + jwt;
    }

    @GetMapping("/verify")
    @TokenBearerAuth
    public String verify() {
        return "OK";
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
