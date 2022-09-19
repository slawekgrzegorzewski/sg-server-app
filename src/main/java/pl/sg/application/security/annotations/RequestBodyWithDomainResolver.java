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
import pl.sg.application.model.WithDomain;
import pl.sg.application.service.AuthorizationService;
import pl.sg.application.transport.WithDomainTO;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
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
        final boolean isWithDomain = Arrays.asList(parameter.getParameterType().getInterfaces()).contains(WithDomain.class);
        return ann != null && isWithDomain;
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
        WithDomainTO withDomainTO = gson.fromJson(body, ann.transportClass());
        handleWithZoneId((ServletWebRequest) webRequest, withDomainTO);
        WithDomain withDomain;
        if (ann.create()) {
            withDomain = map(withDomainTO, parameter.getParameterType(), ann);
            domain = new DomainExtractor(this.entityManager).getDomain(webRequest.getHeader("domainId"));
        } else {
            withDomain = (WithDomain) this.entityManager.find(parameter.getParameterType(), withDomainTO.getId());
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

    private static void handleWithZoneId(ServletWebRequest webRequest, WithDomainTO withDomainTO) throws IllegalAccessException {
        for (Field field : withDomainTO.getClass().getDeclaredFields()) {
            if (field.getType().equals(LocalDateTime.class) && field.isAnnotationPresent(AddZoneIdOffsetDuringDeserialization.class)) {
                ZoneId zoneId = ofNullable(webRequest.getRequest().getHeader("x-timezone-id"))
                        .map(ZoneId::of)
                        .orElseGet(ZoneId::systemDefault);
                BeanWrapper wrapper = new BeanWrapperImpl(withDomainTO);
                ofNullable((LocalDateTime) wrapper.getPropertyValue(field.getName()))
                        .map(localDateTime -> localDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneId).toLocalDateTime())
                        .ifPresent(localDateTime -> wrapper.setPropertyValue(field.getName(), localDateTime));
            }
        }
    }

    private WithDomain map(WithDomainTO source, Class<?> destType, RequestBodyWithDomain ann) {
        WithDomain withDomain;
        if (StringUtils.hasText(ann.mapperName())) {
            withDomain = (WithDomain) this.modelMapper.map(source, destType, ann.mapperName());
        } else {
            withDomain = (WithDomain) this.modelMapper.map(source, destType);
        }
        return withDomain;
    }

    private WithDomain map(WithDomainTO source, WithDomain dest, RequestBodyWithDomain ann) {
        if (StringUtils.hasText(ann.mapperName())) {
            this.modelMapper.map(source, dest, ann.mapperName());
        } else {
            this.modelMapper.map(source, dest);
        }
        return dest;
    }
}
