package pl.sg.application.configuration;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import java.util.UUID;

@Component
public class AddMDCRequestInterceptor implements WebRequestInterceptor {

    @Override
    public void preHandle(WebRequest request) {
        MDC.put("correlationID", UUID.randomUUID().toString());
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) {

    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) {
        MDC.remove("correlationID");
    }
}