package pl.sg.application.security.annotations;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class MapRequestBodyResolver implements HandlerMethodArgumentResolver {

    private final ModelMapper modelMapper;
    private final Gson gson;

    public MapRequestBodyResolver(ModelMapper modelMapper, Gson gson) {
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        MapRequestBody ann = parameter.getParameterAnnotation(MapRequestBody.class);
        return ann != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws IOException {
        final String body = ((ServletWebRequest) webRequest).getRequest()
                .getReader()
                .lines()
                .collect(Collectors.joining(""));

        final MapRequestBody ann = parameter.getParameterAnnotation(MapRequestBody.class);
        Object source = gson.fromJson(body, ann.transportClass());
        return map(source, parameter.getParameterType(), ann);
    }

    private Object map(Object source, Class<?> destType, MapRequestBody ann) {
        Object result;
        if (StringUtils.hasText(ann.mapperName())) {
            result = this.modelMapper.map(source, destType, ann.mapperName());
        } else {
            result = this.modelMapper.map(source, destType);
        }
        return result;
    }
}
