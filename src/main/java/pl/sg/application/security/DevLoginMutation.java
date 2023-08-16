package pl.sg.application.security;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.context.annotation.Profile;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.service.ApplicationUserService;
import pl.sg.application.service.AuthorizationService;
import pl.sg.graphql.schema.types.AuthenticationInfo;
import pl.sg.graphql.schema.types.LoginParameters;

import static pl.sg.utils.GraphqlMappers.mapAuthenticationInfo;

@DgsComponent
@Profile("dev")
public class DevLoginMutation {
    private final ApplicationUserService applicationUserService;
    private final AuthorizationService authorizationService;

    public DevLoginMutation(ApplicationUserService applicationUserService, AuthorizationService authorizationService) {
        this.applicationUserService = applicationUserService;
        this.authorizationService = authorizationService;
    }

    @DgsMutation
    public AuthenticationInfo login(@InputArgument LoginParameters loginParameters) {
        String login = loginParameters.getLogin();
        ApplicationUser firstByLogin = applicationUserService.getByUserLogins(login);
        return mapAuthenticationInfo(
                firstByLogin,
                authorizationService.generateJWTToken(
                        firstByLogin.getId() + ":" + login,
                        firstByLogin.getRoles(),
                        firstByLogin.getDefaultDomain().getId()));
    }
}