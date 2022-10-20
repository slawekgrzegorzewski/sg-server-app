package pl.sg.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    private final ApplicationUserService applicationUserService;
    private final AuthorizationService authorizationService;

    @Autowired
    public DevLoginController(ApplicationUserService applicationUserService, AuthorizationService authorizationService) {
        this.applicationUserService = applicationUserService;
        this.authorizationService = authorizationService;
    }

    @Operation(summary = "Creates a JWT token for user",
            description = "",
            tags = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "JWT Token",
                    content = @Content(mediaType = "plain/text",
                            schema = @Schema(implementation = String.class))),
    })
    @PostMapping(produces = "plain/text")
    public String login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User to create token for",
                    required = true,
                    content = @Content(mediaType = "plain/text",
                            schema = @Schema(implementation = String.class)))
            @RequestBody @NotBlank @Valid String user) {
        ApplicationUser firstByLogin = applicationUserService.getByUserLogins(user);
        return authorizationService.generateJWTToken(
                firstByLogin.getId() + ":" + user,
                firstByLogin.getRoles(),
                firstByLogin.getDefaultDomain().getId());
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
