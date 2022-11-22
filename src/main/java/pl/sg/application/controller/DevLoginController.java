package pl.sg.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.application.service.ApplicationUserService;
import pl.sg.application.service.AuthorizationService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

@RestController
@RequestMapping("/login")
@Slf4j
@CrossOrigin
@Validated
@Profile("dev")
public class DevLoginController {

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";

    private final ApplicationUserService applicationUserService;
    private final AuthorizationService authorizationService;

    @Autowired
    public DevLoginController(ApplicationUserService applicationUserService, AuthorizationService authorizationService) {
        this.applicationUserService = applicationUserService;
        this.authorizationService = authorizationService;
    }

    @PostMapping(produces = "plain/text", path = "/plain")
    public String login(@RequestBody @NotBlank @Valid String user) {
        ApplicationUser firstByLogin = applicationUserService.getByUserLogins(user);
        return authorizationService.generateJWTToken(
                firstByLogin.getId() + ":" + user,
                firstByLogin.getRoles(),
                firstByLogin.getDefaultDomain().getId());
    }

    @PostMapping(produces = "plain/text")
    public String login(@RequestBody @Valid LoginController.User user) {
        ApplicationUser firstByLogin = applicationUserService.getByUserLogins(user.getName());
        String jwt = authorizationService.generateJWTToken(
                firstByLogin.getId() + ":" + user.getName(),
                firstByLogin.getRoles(),
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
}
