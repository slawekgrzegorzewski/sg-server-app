package pl.sg.accountant;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.accountant.service.AccountsService;
import pl.sg.application.repository.DomainRepository;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.types.Account;
import pl.sg.utils.GraphqlMappers;

import java.util.List;

@DgsComponent
public class AccountsDatafetcher {
    private final AccountsService accountsService;
    private final DomainRepository domainRepository;

    public AccountsDatafetcher(AccountsService accountsService, DomainRepository domainRepository) {
        this.accountsService = accountsService;
        this.domainRepository = domainRepository;
    }

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<Account> accounts(@RequestHeader("domainId") int domainId) {
        return accountsService.getForDomain(domainRepository.getReferenceById(domainId))
                .stream()
                .map(GraphqlMappers::mapAccount)
                .toList();
    }
}
