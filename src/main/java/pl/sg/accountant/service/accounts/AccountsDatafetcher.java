package pl.sg.accountant.service.accounts;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.types.Account;
import pl.sg.utils.GraphqlMappers;

import java.util.List;

@DgsComponent
public class AccountsDatafetcher {
    private final AccountsService accountsService;

    public AccountsDatafetcher(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<Account> accounts(@RequestHeader("domainId") int domainId) {
        return accountsService.getForDomain(domainId)
                .stream()
                .map(GraphqlMappers::mapAccount)
                .toList();
    }
}
