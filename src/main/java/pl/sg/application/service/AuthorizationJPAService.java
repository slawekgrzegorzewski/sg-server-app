package pl.sg.application.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;
import pl.sg.application.ForbiddenException;
import pl.sg.application.UnauthorizedException;
import pl.sg.application.configuration.Configuration;
import pl.sg.application.model.ApplicationUser;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class AuthorizationJPAService implements AuthorizationService {
    private static final String ROLES = "roles";
    private static final String DEFAULT_DOMAIN = "defaultDomain";
    private static final Duration TOKEN_DURATION = Duration.ofDays(30);

    private final ApplicationUserService applicationUserService;
    private final Configuration configuration;

    public AuthorizationJPAService(ApplicationUserService applicationUserService, Configuration configuration) {
        this.applicationUserService = applicationUserService;
        this.configuration = configuration;
    }

    @Override
    public void validateRequiredRoles(String token, String[] all, String[] any) {
        DecodedJWT decodedJWT = decodeToken(token);
        validateAll(decodedJWT, all);
        validateAny(decodedJWT, any);
    }

    private void validateAll(DecodedJWT token, String... all) {
        List<String> rolesFromToken = token.getClaim(ROLES).asList(String.class);
        if (all.length > 0 && !Stream.of(all).allMatch(rolesFromToken::contains))
            throw ForbiddenException.allRoleNotMet(extractUserName(token), all);
    }

    private void validateAny(DecodedJWT token, String[] any) {
        List<String> rolesFromToken = token.getClaim(ROLES).asList(String.class);
        if (any.length > 0 && Stream.of(any).noneMatch(rolesFromToken::contains))
            throw ForbiddenException.anyRoleNotMet(extractUserName(token), any);
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

    @Override
    public ApplicationUser getUserInfo(String token) {
        return applicationUserService.findByUserLogins(extractUserName(decodeToken(token)))
                .orElseThrow(() -> new UnauthorizedException("Wrong JWT token"));
    }

    @Override
    public String generateJWTToken(String subject, List<String> roles, int defaultDomainId) {
        return JWT.create()
                .withSubject(subject)
                .withClaim(ROLES, roles)
                .withClaim(DEFAULT_DOMAIN, defaultDomainId)
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_DURATION.toMillis()))
                .sign(HMAC512(configuration.getJWTTokenSecret().getBytes()));
    }

    private String extractUserName(DecodedJWT token) {
        String subject = token.getSubject();
        String[] elements = subject.split(":");
        return subject.replace(elements[0] + ":", "");
    }
}
