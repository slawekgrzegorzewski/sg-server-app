package pl.sg.application.security.annotations;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.sg.application.UnauthorizedException;
import pl.sg.application.service.AuthorizationService;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class TokenBearerAspect {
    private final AuthorizationService authorizationService;

    public TokenBearerAspect(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Before("execution(public * *(..)) && @annotation(pl.sg.application.security.annotations.TokenBearerAuth)")
    public void validateToken(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        TokenBearerAuth tokenBearerAuth = methodSignature.getMethod().getAnnotation(TokenBearerAuth.class);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        if (token == null) {
            throw new UnauthorizedException("No token bearer in the request.");
        }
        authorizationService.validateRequiredRoles(token, tokenBearerAuth.all(), tokenBearerAuth.any());
    }
}
