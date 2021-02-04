package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.service.AccountsService;
import pl.sg.accountant.transport.accounts.AccountTO;
import pl.sg.application.ForbiddenException;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.security.annotations.RequestUser;
import pl.sg.application.security.annotations.TokenBearerAuth;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
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
    @GetMapping("/mine")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<AccountTO> userAccount(@RequestUser ApplicationUser user) {
        return map(accountsService.getForUser(user));
    }

    private List<AccountTO> map(List<Account> accounts) {
        return accounts.stream().map(a -> mapper.map(a, AccountTO.class)).collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public AccountTO createAccount(
            @RequestBody @Valid AccountTO account,
            @RequestUser(RequestUser.LOGIN) String login) {
        Account toCreate = mapper.map(account, Account.class);
        accountsService.createAccount(toCreate, login);
        return mapper.map(toCreate, AccountTO.class);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ResponseEntity<String> updateAccount(
            @RequestBody @Valid AccountTO account,
            @RequestUser(RequestUser.LOGIN) String login) {
        return accountsService.findById(account.getId())
                .filter(toEdit -> toEdit.getApplicationUser().getLoggedInUser().getLogin().equals(login))
                .map(toEdit -> update(toEdit, account))
                .map(toEdit -> {
                    accountsService.update(toEdit);
                    return ResponseEntity.ok("OK");
                }).orElseGet(() -> ResponseEntity.badRequest().body("Account to edit does not exist or do not belong to you"));
    }

    private Account update(Account toEdit, AccountTO source) {
        toEdit.setName(source.getName());
        return toEdit;
    }

    @Override
    @DeleteMapping("/{id}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ResponseEntity<String> deleteAccount(
            @PathVariable Integer id,
            @RequestUser(RequestUser.LOGIN) String login) {
        final Optional<Account> toDelete = accountsService.findById(id);
        if (toDelete.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity with id " + id + " not found.");
        }
        if (toDelete.get().getApplicationUser().getLoggedInUser().getLogin().equals(login)) {
            accountsService.delete(toDelete.get());
            return ResponseEntity.ok("Account deleted");
        } else {
            throw new ForbiddenException("You can only delete accounts which belongs to you");
        }
    }
}
