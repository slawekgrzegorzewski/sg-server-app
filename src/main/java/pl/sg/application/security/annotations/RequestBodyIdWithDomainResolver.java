package pl.sg.application.security.annotations;

import com.google.gson.Gson;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.WithDomain;
import pl.sg.application.service.AuthorizationService;

import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RequestBodyIdWithDomainResolver implements HandlerMethodArgumentResolver {

    private AuthorizationService authorizationService;
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public RequestBodyIdWithDomainResolver(AuthorizationService authorizationService, EntityManager entityManager, ModelMapper modelMapper, Gson gson) {
        this.authorizationService = authorizationService;
        this.entityManager = entityManager;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        RequestBodyIdWithDomain ann = parameter.getParameterAnnotation(RequestBodyIdWithDomain.class);
        final boolean isWithDomain = Arrays.asList(parameter.getParameterType().getInterfaces()).contains(WithDomain.class);
        return ann != null && isWithDomain;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws IOException {
        final String body = ((ServletWebRequest) webRequest).getRequest()
                .getReader()
                .lines()
                .collect(Collectors.joining("\n"));

        final RequestBodyIdWithDomain ann = parameter.getParameterAnnotation(RequestBodyIdWithDomain.class);
        int id = Integer.parseInt(body);
        WithDomain withDomain = (WithDomain) this.entityManager.find(parameter.getParameterType(), id);

        if (withDomain == null) {
            if (ann.required()) {
                throw new EntityNotFoundException(parameter.getParameterType() + " with id " + id);
            }
            return null;
        }

        ApplicationUser user = authorizationService.getUserInfo(Objects.requireNonNull(webRequest.getHeader("Authorization")));

        if (ann.domainMember()) {
            user.validateDomain(withDomain.getDomain());
        }
        if (ann.domainAdmin()) {
            user.validateAdminDomain(withDomain.getDomain());
        }
        return withDomain;
    }
}
