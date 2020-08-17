package pl.sg.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import pl.sg.application.ForbiddenException;
import pl.sg.application.UnauthorizedException;
import pl.sg.application.configuration.Configuration;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ApplicationUserRepository;

import java.time.Duration;
import java.util.Date;
import java.util.List;

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

    public DecodedJWT validateAll(String token, String... all) {
        DecodedJWT decodedJWT = decodeToken(token);
        List<String> rolesFromToken = decodedJWT.getClaim(ROLES).asList(String.class);
        if (all.length > 0 && !Stream.of(all).allMatch(rolesFromToken::contains))
            throw ForbiddenException.allRoleNotMet(decodedJWT.getSubject(), all);
        return decodedJWT;
    }

    public DecodedJWT validateAny(String token, String[] any) {
        DecodedJWT decodedJWT = decodeToken(token);
        List<String> rolesFromToken = decodedJWT.getClaim(ROLES).asList(String.class);
        if (any.length > 0 && Stream.of(any).noneMatch(rolesFromToken::contains))
            throw ForbiddenException.anyRoleNotMet(decodedJWT.getSubject(), any);
        return decodedJWT;
    }

    private DecodedJWT decodeToken(String token) {
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
        DecodedJWT decodedJWT = validateAll(token);
        return applicationUserRepository.findFirstByLogin(decodedJWT.getSubject())
                .orElseThrow(() -> new UnauthorizedException("Wrong JWT token"));
    }

    public String generateJWTToken(String user, List<String> roles) {
        return JWT.create()
                .withSubject(user)
                .withClaim(ROLES, roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + DURATION.toMillis()))
                .sign(HMAC512(configuration.getJWTTokenSecret().getBytes()));
    }
}
