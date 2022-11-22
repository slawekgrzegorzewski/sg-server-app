package pl.sg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import pl.sg.application.service.AuthorizationService;

import java.util.List;

public abstract class AbstractApplicationBaseTest extends AbstractContainerBaseTest {

    protected static final int DEFAULT_DOMAIN_ID = 1;
    protected static final int USER_ID = 1;
    protected static final String USER_NAME = "slawek";

    @Autowired
    private AuthorizationService authorizationService;

    protected HttpHeaders headers(int domainId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("domainId", String.valueOf(domainId));
        return headers;
    }

    protected HttpHeaders authenticatedHeaders(int domainId, String... roles) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("domainId", String.valueOf(domainId));
        headers.setBearerAuth(authorizationService.generateJWTToken(USER_ID + ":" + USER_NAME, List.of(roles), DEFAULT_DOMAIN_ID));
        return headers;
    }
}
