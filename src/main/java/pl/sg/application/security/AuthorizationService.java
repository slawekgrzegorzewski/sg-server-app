package pl.sg.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;
import pl.sg.application.ForbiddenException;
import pl.sg.application.UnauthorizedException;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ApplicationUserRepository;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class AuthorizationService {
    private static final String ROLES = "roles";
    private static final String SECRET = "SecretKeyToGenJWTs";
    private static final Duration DURATION = Duration.ofMinutes(10);

    private final ApplicationUserRepository applicationUserRepository;

    public AuthorizationService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    public ApplicationUser validate(String token, String... roles) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT.require(HMAC512(SECRET.getBytes()))
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
        return applicationUserRepository.findFirstByLogin(decodedJWT.getSubject()).orElseThrow(() -> new UnauthorizedException("Wrong JWT token"));
    }

    public String generateJWTToken(String user, List<String> roles) {
        return JWT.create()
                .withSubject(user)
                .withClaim(ROLES, roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + DURATION.toMillis()))
                .sign(HMAC512(SECRET.getBytes()));
    }
}
