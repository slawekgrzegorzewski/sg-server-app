package pl.sg.application.security.annotations;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.sg.application.UnauthorizedException;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;
import pl.sg.application.service.AuthorizationService;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class TokenBearerAspect {
    private final AuthorizationService authorizationService;
    private final EntityManager entityManager;

    public TokenBearerAspect(AuthorizationService authorizationService, EntityManager entityManager) {
        this.authorizationService = authorizationService;
        this.entityManager = entityManager;
    }

    @Before("execution(public * *(..)) && @annotation(pl.sg.application.security.annotations.TokenBearerAuth)")
    public void validateToken(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        TokenBearerAuth tokenBearerAuth = methodSignature.getMethod().getAnnotation(TokenBearerAuth.class);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        String token = tokenBearerAuth.inQuery()
                ? request.getParameter("authorization")
                : request.getHeader("Authorization");
        if (token == null) {
            throw new UnauthorizedException("No token bearer in the request.");
        }
        authorizationService.validateRequiredRoles(token, tokenBearerAuth.all(), tokenBearerAuth.any());
        if (tokenBearerAuth.domainAdmin() || tokenBearerAuth.domainMember()) {
            final ApplicationUser userInfo = authorizationService.getUserInfo(token);
            final Domain domain = new DomainExtractor(this.entityManager).getDomain(
                    tokenBearerAuth.inQuery()
                            ? request.getParameter("domainId")
                            : request.getHeader("DomainId")
            );
            if (tokenBearerAuth.domainMember()) {
                userInfo.validateDomain(domain);
            }
            if (tokenBearerAuth.domainAdmin()) {
                userInfo.validateAdminDomain(domain);
            }
        }
    }
}
