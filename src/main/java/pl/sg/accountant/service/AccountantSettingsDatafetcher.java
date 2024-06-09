package pl.sg.accountant.service;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.accountant.service.accounts.AccountsService;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.DgsConstants;
import pl.sg.graphql.schema.types.AccountantSettings;
import pl.sg.utils.GraphqlMappers;

@DgsComponent
public class AccountantSettingsDatafetcher {
    private final AccountantSettingsService accountantSettingsService;

    public AccountantSettingsDatafetcher(AccountantSettingsService accountantSettingsService) {
        this.accountantSettingsService = accountantSettingsService;
    }

    @DgsData(parentType = DgsConstants.SETTINGS.TYPE_NAME, field = DgsConstants.SETTINGS.AccountantSettings)
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public AccountantSettings accountantSettings(@RequestHeader("domainId") int domainId) {
        return AccountantSettings.newBuilder()
                .isCompany(accountantSettingsService.getForDomain(domainId).isCompany())
                .build();
    }
}
