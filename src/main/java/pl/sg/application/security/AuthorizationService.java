package pl.sg.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;
import pl.sg.application.ForbiddenException;
import pl.sg.application.UnauthorizedException;
import pl.sg.application.configuration.Configuration;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ApplicationUserRepository;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class AuthorizationService {
    private static final String ROLES = "roles";
    private static final Duration DURATION = Duration.ofMinutes(20);

    private final ApplicationUserRepository applicationUserRepository;
    private final Configuration configuration;

    public AuthorizationService(ApplicationUserRepository applicationUserRepository, Configuration configuration) {
        this.applicationUserRepository = applicationUserRepository;
        this.configuration = configuration;
    }

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

    public ApplicationUser getUserInfo(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        ApplicationUser applicationUser = applicationUserRepository.findFirstByUserLogins(extractUserName(decodedJWT))
                .orElseThrow(() -> new UnauthorizedException("Wrong JWT token"));
        applicationUser.setLoggedInUser(extractUserName(decodedJWT));
        return applicationUser;
    }

    public String generateJWTToken(String subject, List<String> roles) {
        return JWT.create()
                .withSubject(subject)
                .withClaim(ROLES, roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + DURATION.toMillis()))
                .sign(HMAC512(configuration.getJWTTokenSecret().getBytes()));
    }

    private int extractUserId(DecodedJWT token) {
        String[] elements = token.getSubject().split(":");
        return Integer.parseInt(elements[0]);
    }

    private String extractUserName(DecodedJWT token) {
        String subject = token.getSubject();
        String[] elements = subject.split(":");
        return subject.replace(elements[0] + ":", "");
    }
}
