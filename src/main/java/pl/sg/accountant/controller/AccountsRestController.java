package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.Account;
import pl.sg.accountant.model.FinancialTransaction;
import pl.sg.accountant.service.AccountsService;
import pl.sg.accountant.service.AccountstException;
import pl.sg.accountant.transport.AccountTO;
import pl.sg.accountant.transport.TransactionTO;
import pl.sg.application.ForbiddenException;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.security.annotations.RequestUser;
import pl.sg.application.security.annotations.TokenBearerAuth;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
public class AccountsRestController implements AccountsController {
    private final AccountsService accountsService;
    private final ModelMapper mapper;

    public AccountsRestController(AccountsService accountsService, ModelMapper mapper) {
        this.accountsService = accountsService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<List<AccountTO>> accounts(@RequestUser ApplicationUser user) {
        final List<String> roles = user.getRoles();
        if (roles.contains("ADMIN"))
            return ResponseEntity.ok(map(accountsService.getAll()));
        return ResponseEntity.ok(map(accountsService.getForUser(user.getLogin())));
    }

    private List<AccountTO> map(List<Account> accounts) {
        return accounts.stream().map(a -> mapper.map(a, AccountTO.class)).collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<AccountTO> createAccount(
            @RequestBody @Valid AccountTO account,
            @RequestUser(RequestUser.LOGIN) String login) {
        Account toCreate = mapper.map(account, Account.class);
        accountsService.createAccount(toCreate, login);
        return ResponseEntity.ok(mapper.map(toCreate, AccountTO.class));
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<String> updateAccount(
            @RequestBody @Valid AccountTO account,
            @RequestUser(RequestUser.LOGIN) String login) {
        return accountsService.findById(account.getId())
                .filter(toEdit -> toEdit.getApplicationUser().getLogin().equals(login))
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
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<String> deleteAccount(
            @PathVariable Integer id,
            @RequestUser(RequestUser.LOGIN) String login) {
        final Optional<Account> toDelete = accountsService.findById(id);
        if (toDelete.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity with id " + id + " not found.");
        }
        if (toDelete.get().getApplicationUser().getLogin().equals(login)) {
            accountsService.delete(toDelete.get());
            return ResponseEntity.ok("Account deleted");
        } else {
            throw new ForbiddenException("You can only delete accounts which belongs to you");
        }
    }

    @Override
    @PostMapping("/transfer/{from}/{to}/{amount}")
    @TokenBearerAuth(all = "ADMIN")
    public ResponseEntity<TransactionTO> transfer(
            @PathVariable("from") int fromId,
            @PathVariable("to") int toId,
            @PathVariable("amount") BigDecimal amount,
            @RequestBody String description,
            @RequestUser(RequestUser.LOGIN) String login) throws AccountstException {
        FinancialTransaction result = accountsService.transferMoneyWithoutConversion(
                fromId,
                toId,
                amount,
                description,
                login);
        return ResponseEntity.ok(mapper.map(result, TransactionTO.class));
    }
}
