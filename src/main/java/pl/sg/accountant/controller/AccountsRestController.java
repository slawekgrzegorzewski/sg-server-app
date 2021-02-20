package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.service.AccountsService;
import pl.sg.accountant.transport.accounts.AccountTO;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.PathVariableWithDomain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static pl.sg.Application.CREATE_ACCOUNT;
import static pl.sg.Application.UPDATE_ACCOUNT;

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
    @GetMapping("/mine")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<AccountTO> domainAccount(@RequestDomain Domain domain) {
        return map(accountsService.getForDomain(domain));
    }

    private List<AccountTO> map(List<Account> accounts) {
        return accounts.stream().map(a -> mapper.map(a, AccountTO.class)).collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"}, domainAdmin = true)
    public AccountTO createAccount(
            @RequestBodyWithDomain(
                    transportClass = AccountTO.class,
                    mapperName = CREATE_ACCOUNT,
                    create = true,
                    domainAdmin = true)
            @Valid Account account
    ) {
        accountsService.createAccount(account);
        return mapper.map(account, AccountTO.class);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String updateAccount(
            @RequestBodyWithDomain(transportClass = AccountTO.class, mapperName = UPDATE_ACCOUNT) @Valid Account account
    ) {
        accountsService.update(account);
        return "OK";
    }

    @Override
    @DeleteMapping("/{account}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String deleteAccount(@PathVariableWithDomain(requireAdmin = true) Account account) {
        accountsService.delete(account);
        return "Deleted.";
    }
}
