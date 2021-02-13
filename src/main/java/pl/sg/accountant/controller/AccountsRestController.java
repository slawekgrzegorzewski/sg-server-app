package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.service.AccountsService;
import pl.sg.accountant.transport.accounts.AccountTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.security.annotations.RequestUser;
import pl.sg.application.security.annotations.TokenBearerAuth;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
@Validated
public class AccountsRestController implements AccountsController {
    private final AccountsService accountsService;
    private final ModelMapper mapper;

    public AccountsRestController(AccountsService accountsService, ModelMapper mapper) {
        this.accountsService = accountsService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN"})
    public List<AccountTO> allAccounts() {
        return map(accountsService.getAll());
    }

    @Override
    @GetMapping("/mine/{domainId}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<AccountTO> userAccount(@RequestUser ApplicationUser user,
                                       @PathVariable int domainId) {
        return map(accountsService.getForUserAndDomain(user, domainId));
    }

    private List<AccountTO> map(List<Account> accounts) {
        return accounts.stream().map(a -> mapper.map(a, AccountTO.class)).collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public AccountTO createAccount(@RequestUser ApplicationUser user,
                                   @RequestBody @Valid AccountTO account) {
        Account toCreate = mapper.map(account, Account.class);
        accountsService.createAccount(user, toCreate);
        return mapper.map(toCreate, AccountTO.class);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String updateAccount(@RequestUser ApplicationUser user,
                                                @RequestBody @Valid AccountTO account) {
        final Account byId = accountsService.getById(user, account.getId());
        byId.setName(account.getName());
        accountsService.update(user, byId);
        return "OK";
    }

    @Override
    @DeleteMapping("/{id}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String deleteAccount(
            @PathVariable Integer id,
            @RequestUser ApplicationUser user) {
        accountsService.delete(user, id);
        return "Deleted.";
    }
}
