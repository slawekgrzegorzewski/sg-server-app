package pl.sg.accountant.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class GoogleAuthoritiesExtractor implements AuthoritiesExtractor {

//    @Autowired
//    public UserService userService;

    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
        String username = (String) map.get("id");
//        CurrentUser user = userService.getByFacebookId(id);
//        if (user == null) {
//            return Collections.<GrantedAuthority> emptyList();
//        }
        return AuthorityUtils.createAuthorityList("A", "B");
    }
}