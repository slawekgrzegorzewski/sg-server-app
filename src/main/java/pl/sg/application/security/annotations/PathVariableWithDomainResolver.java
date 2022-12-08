package pl.sg.application.security.annotations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMapMethodArgumentResolver;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.WithDomain;
import pl.sg.application.service.AuthorizationService;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Component
public class PathVariableWithDomainResolver extends PathVariableMapMethodArgumentResolver {

    private final AuthorizationService authorizationService;
    private final EntityManager entityManager;

    public PathVariableWithDomainResolver(AuthorizationService authorizationService, EntityManager entityManager) {
        this.authorizationService = authorizationService;
        this.entityManager = entityManager;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        PathVariableWithDomain ann = parameter.getParameterAnnotation(PathVariableWithDomain.class);
        final boolean isWithDomain = Arrays.asList(parameter.getParameterType().getInterfaces()).contains(WithDomain.class);
        return ann != null && isWithDomain;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        Map<String, String> uriTemplateVars = (Map<String, String>) webRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);

        final PathVariableWithDomain ann = parameter.getParameterAnnotation(PathVariableWithDomain.class);
        String varName = ann.value();
        if (!StringUtils.hasText(varName)) {
            varName = parameter.getParameterName();
        }
        final String id = uriTemplateVars.get(varName);
        WithDomain wd = (WithDomain) this.entityManager.find(
                parameter.getParameterType(),
                Integer.parseInt(id));

        if (wd == null) {
            if (ann.required()) {
                throw new EntityNotFoundException(parameter.getParameterType() + " with id " + id);
            }
            return null;
        }

        String token = webRequest.getHeader("Authorization");
        ApplicationUser user = authorizationService.getUserInfo(Objects.requireNonNull(token));

        if (ann.requireMember()) {
            user.validateDomain(wd.getDomain());
        }
        if (ann.requireAdmin()) {
            user.validateAdminDomain(wd.getDomain());
        }

        return wd;
    }
}
