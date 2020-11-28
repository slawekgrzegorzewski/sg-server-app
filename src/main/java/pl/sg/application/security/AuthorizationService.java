package pl.sg.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;
import pl.sg.application.ForbiddenException;
import pl.sg.application.UnauthorizedException;
import pl.sg.application.configuration.Configuration;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ApplicationUserLogin;
import pl.sg.application.model.ApplicationUserRepository;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class AuthorizationService {
    private static final String ROLES = "roles";
    private static final Duration DURATION = Duration.ofDays(10 * 365);

    private final ApplicationUserRepository applicationUserRepository;
    private final Configuration configuration;

    public AuthorizationService(ApplicationUserRepository applicationUserRepository, Configuration configuration) {
        this.applicationUserRepository = applicationUserRepository;
        this.configuration = configuration;
    }

    public void validateAll(DecodedJWT token, String... all) {
        List<String> rolesFromToken = token.getClaim(ROLES).asList(String.class);
        if (all.length > 0 && !Stream.of(all).allMatch(rolesFromToken::contains))
            throw ForbiddenException.allRoleNotMet(token.getSubject(), all);
    }

    public void validateAny(DecodedJWT token, String[] any) {
        List<String> rolesFromToken = token.getClaim(ROLES).asList(String.class);
        if (any.length > 0 && Stream.of(any).noneMatch(rolesFromToken::contains))
            throw ForbiddenException.anyRoleNotMet(token.getSubject(), any);
    }

    public DecodedJWT decodeToken(String token) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT.require(HMAC512(configuration.getJWTTokenSecret().getBytes()))
                    .build()
                    .verify(token.replace("Bearer ", ""));
        } catch (JWTVerificationException ex) {
            throw new UnauthorizedException("Token validation error");
        }
        return decodedJWT;
    }

    public ApplicationUser getUserInfo(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        ApplicationUser applicationUser = applicationUserRepository.findFirstByUserLogins(decodedJWT.getSubject())
                .orElseThrow(() -> new UnauthorizedException("Wrong JWT token"));
        setLoggedInUser(applicationUser, decodedJWT.getSubject());
        return applicationUser;
    }

    public void setLoggedInUser(ApplicationUser applicationUser, String userName) {
        ApplicationUserLogin userLogin = applicationUser.getUserLogins().stream()
                .filter(ul -> ul.getLogin().equals(userName)).findAny()
                .get();
        applicationUser.setLoggedInUser(userLogin);
    }

    public String generateJWTToken(String user, List<String> roles) {
        return JWT.create()
                .withSubject(user)
                .withClaim(ROLES, roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + DURATION.toMillis()))
                .sign(HMAC512(configuration.getJWTTokenSecret().getBytes()));
    }
}
