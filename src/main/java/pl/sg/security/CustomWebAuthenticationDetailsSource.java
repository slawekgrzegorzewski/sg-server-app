package pl.sg.security;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class CustomWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new CustomWebAuthenticationDetails(context);
    }

    public static class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

        private String verificationCode;

        public CustomWebAuthenticationDetails(HttpServletRequest request) {
            super(request);
            verificationCode = request.getParameter("x-tfa");
        }

        public String getVerificationCode() {
            return verificationCode;
        }
    }
}