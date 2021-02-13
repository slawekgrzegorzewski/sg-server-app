package pl.sg.application.security.annotations;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.service.AuthorizationService;

import java.util.Objects;

@Component
public class RequestUserResolver implements HandlerMethodArgumentResolver {

    private final AuthorizationService authorizationService;

    public RequestUserResolver(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(RequestUser.class) != null;
    }

    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        String token = webRequest.getHeader("Authorization");
        ApplicationUser user = authorizationService.getUserInfo(Objects.requireNonNull(token));
        RequestUser attr = parameter.getParameterAnnotation(RequestUser.class);
        assert attr != null;
        return new ApplicationUserFieldExtractor<>(parameter.getParameterType()).extract(user, attr.value());
    }
}