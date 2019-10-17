package pl.sg.accountant.oauth;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class GoogleController {

    @RequestMapping(value = "/user")
//    @Secured("B")
    @PreAuthorize("authentication.principal.roles.contains('B')")
    public Principal user(OAuth2Authentication principal) {
        return principal;
    }
}