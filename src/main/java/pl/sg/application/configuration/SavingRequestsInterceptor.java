package pl.sg.application.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ReceivedRequest;
import pl.sg.application.service.AuthorizationService;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SavingRequestsInterceptor implements WebRequestInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(SavingRequestsInterceptor.class);

    private final AuthorizationService authorizationService;
    private final ObjectMapper objectMapper;

    public SavingRequestsInterceptor(AuthorizationService authorizationService, ObjectMapper objectMapper) {
        this.authorizationService = authorizationService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void preHandle(@NotNull WebRequest request) throws Exception {
        if (request instanceof DispatcherServletWebRequest) {
            HttpServletRequest r = ((DispatcherServletWebRequest) request).getRequest();
            String method = r.getMethod();
            String requestURI = r.getRequestURI();
            if ("/error".equals(requestURI)) {
                return;
            }
            Map<String, String> headers = new HashMap<>();
            Enumeration<String> headerNames = r.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                if (name.equals("authorization")) {
                    headers.put(name, "***redacted***");
                } else {
                    headers.put(name, r.getHeader(name));
                }
            }
            String body = r.getReader().lines().collect(Collectors.joining("\n"));
            if ("/login".equals(requestURI)) {
                body = body.replaceFirst("\"pass\":[^\"]*\"[^\"]*\"", "\"pass\":\"removed\"");
            }
            String token = request.getHeader("Authorization");
            String login = "";
            if (token != null) {
                login = authorizationService.getUserInfo(token).getLogin();
            }
            String remoteAddr = r.getRemoteAddr();

            ReceivedRequest receivedRequest = new ReceivedRequest()
                    .setMethod(method)
                    .setHeaders(headers.toString())
                    .setRequestURI(requestURI)
                    .setRemoteAddress(remoteAddr)
                    .setLogin(login)
                    .setBody(body);
            LOG.trace(this.objectMapper.writeValueAsString(receivedRequest));
        }
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) {
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) {
    }
}
