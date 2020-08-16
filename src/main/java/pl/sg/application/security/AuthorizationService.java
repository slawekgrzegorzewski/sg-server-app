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

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class AuthorizationService {
    private static final String ROLES = "roles";
    private static final Duration DURATION = Duration.ofHours(10);

    private final ApplicationUserRepository applicationUserRepository;
    private final Configuration configuration;

    public AuthorizationService(ApplicationUserRepository applicationUserRepository, Configuration configuration) {
        this.applicationUserRepository = applicationUserRepository;
        this.configuration = configuration;
    }

    public DecodedJWT validate(String token, String... roles) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT.require(HMAC512(configuration.getJWTTokenSecret().getBytes()))
                    .build()
                    .verify(token.replace("Bearer ", ""));
        } catch (JWTVerificationException ex) {
            throw new UnauthorizedException("Token validation error");
        }
        List<String> rolesFromToken = decodedJWT.getClaim(ROLES).asList(String.class);
        for (String role : roles) {
            if (!rolesFromToken.contains(role)) {
                throw new ForbiddenException(decodedJWT.getSubject(), role);
            }
        }
        return decodedJWT;
    }

    public ApplicationUser getUserInfo(String token) {
        DecodedJWT decodedJWT = validate(token);
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
