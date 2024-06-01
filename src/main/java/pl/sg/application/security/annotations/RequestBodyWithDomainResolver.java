package pl.sg.application.security.annotations;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;
import pl.sg.application.service.AuthorizationService;
import pl.sg.application.api.WithDomain;

import jakarta.persistence.EntityManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Component
public class RequestBodyWithDomainResolver implements HandlerMethodArgumentResolver {

    private AuthorizationService authorizationService;
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public RequestBodyWithDomainResolver(AuthorizationService authorizationService, EntityManager entityManager, ModelMapper modelMapper, Gson gson) {
        this.authorizationService = authorizationService;
        this.entityManager = entityManager;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        RequestBodyWithDomain ann = parameter.getParameterAnnotation(RequestBodyWithDomain.class);
        final boolean isWithDomain = findAllInterfaces(parameter.getParameterType()).contains(pl.sg.application.model.WithDomain.class);
        return ann != null && isWithDomain;
    }

    private List<Class<?>> findAllInterfaces(Class<?> parameterType) {
        List<Class<?>> interfaces = new ArrayList<>();
        interfaces.addAll(Arrays.stream(parameterType.getInterfaces()).toList());
        if (parameterType.getSuperclass() != null) {
            interfaces.addAll(findAllInterfaces(parameterType.getSuperclass()));
        }
        return interfaces;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws IOException, IllegalAccessException {
        final String body = ((ServletWebRequest) webRequest).getRequest()
                .getReader()
                .lines()
                .collect(Collectors.joining("\n"));
        final RequestBodyWithDomain ann = parameter.getParameterAnnotation(RequestBodyWithDomain.class);
        Domain domain;
        WithDomain withDomainTO = gson.fromJson(body, ann.transportClass());
        handleWithZoneId((ServletWebRequest) webRequest, withDomainTO);
        pl.sg.application.model.WithDomain withDomain;
        if (ann.create()) {
            withDomain = map(withDomainTO, parameter.getParameterType(), ann);
            domain = new DomainExtractor(this.entityManager).getDomain(webRequest.getHeader("domainId"));
        } else {
            withDomain = (pl.sg.application.model.WithDomain) this.entityManager.find(parameter.getParameterType(), withDomainTO.getId());
            domain = withDomain.getDomain();
            map(withDomainTO, withDomain, ann);
        }
        withDomain.setDomain(domain);

        String token = webRequest.getHeader("Authorization");
        ApplicationUser user = authorizationService.getUserInfo(Objects.requireNonNull(token));

        if (ann.domainMember()) {
            user.validateDomain(withDomain.getDomain());
        }
        if (ann.domainAdmin()) {
            user.validateAdminDomain(withDomain.getDomain());
        }
        return withDomain;
    }

    private static void handleWithZoneId(ServletWebRequest webRequest, WithDomain withDomain) throws IllegalAccessException {
        for (Field field : withDomain.getClass().getDeclaredFields()) {
            if (field.getType().equals(LocalDateTime.class) && field.isAnnotationPresent(AddZoneIdOffsetDuringDeserialization.class)) {
                ZoneId zoneId = ofNullable(webRequest.getRequest().getHeader("x-timezone-id"))
                        .map(ZoneId::of)
                        .orElseGet(ZoneId::systemDefault);
                BeanWrapper wrapper = new BeanWrapperImpl(withDomain);
                ofNullable((LocalDateTime) wrapper.getPropertyValue(field.getName()))
                        .map(localDateTime -> localDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneId).toLocalDateTime())
                        .ifPresent(localDateTime -> wrapper.setPropertyValue(field.getName(), localDateTime));
            }
        }
    }

    private pl.sg.application.model.WithDomain map(WithDomain source, Class<?> destType, RequestBodyWithDomain ann) {
        pl.sg.application.model.WithDomain withDomain;
        if (StringUtils.hasText(ann.mapperName())) {
            withDomain = (pl.sg.application.model.WithDomain) this.modelMapper.map(source, destType, ann.mapperName());
        } else {
            withDomain = (pl.sg.application.model.WithDomain) this.modelMapper.map(source, destType);
        }
        return withDomain;
    }

    private pl.sg.application.model.WithDomain map(WithDomain source, pl.sg.application.model.WithDomain dest, RequestBodyWithDomain ann) {
        if (StringUtils.hasText(ann.mapperName())) {
            this.modelMapper.map(source, dest, ann.mapperName());
        } else {
            this.modelMapper.map(source, dest);
        }
        return dest;
    }
}
