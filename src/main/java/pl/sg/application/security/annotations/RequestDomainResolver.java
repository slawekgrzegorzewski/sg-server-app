package pl.sg.application.security.annotations;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.persistence.EntityManager;

@Component
public class RequestDomainResolver implements HandlerMethodArgumentResolver {

    private final EntityManager entityManager;

    public RequestDomainResolver(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(RequestDomain.class) != null;
    }

    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        return new DomainExtractor(this.entityManager).getDomain(webRequest.getHeader("domainId"));
    }
}