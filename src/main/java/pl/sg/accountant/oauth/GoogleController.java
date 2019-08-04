package pl.sg.accountant.oauth;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class GoogleController {

    @RequestMapping(value = "/user")
    public Principal user(OAuth2Authentication principal) {
        return principal;
    }
}