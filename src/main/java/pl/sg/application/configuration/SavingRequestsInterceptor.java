package pl.sg.application.configuration;

import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ReceivedRequest;
import pl.sg.application.repository.ReceivedRequestRepository;
import pl.sg.application.service.AuthorizationService;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SavingRequestsInterceptor implements WebRequestInterceptor {

    private final AuthorizationService authorizationService;
    private final ReceivedRequestRepository receivedRequestRepository;

    public SavingRequestsInterceptor(AuthorizationService authorizationService, ReceivedRequestRepository receivedRequestRepository) {
        this.authorizationService = authorizationService;
        this.receivedRequestRepository = receivedRequestRepository;
    }

    @Override
    public void preHandle(WebRequest request) throws Exception {
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
                headers.put(name, r.getHeader(name));
            }
            String body = r.getReader().lines().collect(Collectors.joining("\n"));
            String token = request.getHeader("Authorization");
            String login = "";
            if (token != null) {
                ApplicationUser userInfo = authorizationService.getUserInfo(token);
                login = userInfo.getLogin();
            }
            String remoteAddr = r.getRemoteAddr();

            ReceivedRequest receivedRequest = new ReceivedRequest()
                    .setMethod(method)
                    .setHeaders(headers.toString())
                    .setRequestURI(requestURI)
                    .setRemoteAddress(remoteAddr)
                    .setLogin(login)
                    .setBody(body);
            this.receivedRequestRepository.save(receivedRequest);
        }
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
    }
}
