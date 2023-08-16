package pl.sg.application.security;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.service.ApplicationUserService;
import pl.sg.application.service.AuthorizationService;
import pl.sg.graphql.schema.types.AuthenticationInfo;
import pl.sg.graphql.schema.types.LoginParameters;

import static pl.sg.utils.GraphqlMappers.mapAuthenticationInfo;

@DgsComponent
@Profile("https")
public class LoginMutation {
    private final ApplicationUserService applicationUserService;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;

    public LoginMutation(ApplicationUserService applicationUserService, PasswordEncoder passwordEncoder, AuthorizationService authorizationService) {
        this.applicationUserService = applicationUserService;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
    }

    @DgsMutation
    public AuthenticationInfo login(@InputArgument LoginParameters loginParameters) {
        String login = loginParameters.getLogin();
        String password = loginParameters.getPassword();

        if (login == null || password == null || "".equals(login.trim()) || "".equals(password.trim())) {
            throw new BadCredentialsException("user/password is required");
        }

        ApplicationUser firstByLogin = applicationUserService.getByUserLogins(login);

        if (!passwordEncoder.matches(password, firstByLogin.getPassword())) {
            throw new BadCredentialsException("Wrong user/password");
        }

        if (!new Totp(firstByLogin.getSecret()).verify(loginParameters.getOtp())) {
            throw new BadCredentialsException("Wrong 2FA code");
        }

        return mapAuthenticationInfo(
                firstByLogin,
                authorizationService.generateJWTToken(
                        firstByLogin.getId() + ":" + login,
                        firstByLogin.getRoles(),
                        firstByLogin.getDefaultDomain().getId()));
    }
}