package pl.sg.accountant.oauth;

import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GooglePrincipalExtractor implements PrincipalExtractor {

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String token = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
        User user = new User(
                (String) map.get("id"),
                (String) map.get("given_name"),
                (String) map.get("family_name"),
                (String) map.get("email")
        );
        user.setRoles("A", "B");
        return user;
    }
}
