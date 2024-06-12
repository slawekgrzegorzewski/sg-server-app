package pl.sg.accountant.service.accounts;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.types.Account;
import pl.sg.graphql.schema.types.AccountCreationInput;
import pl.sg.graphql.schema.types.AccountUpdateInput;
import pl.sg.utils.GraphqlMappers;

import java.util.List;
import java.util.UUID;

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

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Account createAccount(
            @RequestHeader("domainId") int domainId,
            @InputArgument AccountCreationInput accountCreationInput) {
        return GraphqlMappers.mapAccount(
                accountsService.createAccount(
                        domainId,
                        accountCreationInput.getName(),
                        accountCreationInput.getCurrentBalance(),
                        accountCreationInput.getCreditLimit(),
                        accountCreationInput.getVisible(),
                        accountCreationInput.getBankAccountId(),
                        accountCreationInput.getBalanceIndex()
                )
        );
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Account updateAccount(
            @RequestHeader("domainId") int domainId,
            @InputArgument AccountUpdateInput accountUpdateInput) {
        return GraphqlMappers.mapAccount(
                accountsService.update(
                        domainId,
                        accountUpdateInput.getPublicId(),
                        accountUpdateInput.getName(),
                        accountUpdateInput.getCurrentBalance(),
                        accountUpdateInput.getCreditLimit(),
                        accountUpdateInput.getVisible(),
                        accountUpdateInput.getBankAccountId(),
                        accountUpdateInput.getBalanceIndex()
                )
        );
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String deleteAccount(
            @RequestHeader("domainId") int domainId,
            @InputArgument UUID accountPublicId) {
        accountsService.delete(
                domainId,
                accountPublicId
        );
        return "OK";
    }
}
