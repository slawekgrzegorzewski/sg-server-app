package pl.sg.application.service;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.types.Settings;

@DgsComponent
public class SettingsDatafetcher {

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Settings settings(@RequestHeader("domainId") int domainId) {
        return Settings.newBuilder()
                .build();
    }
}
