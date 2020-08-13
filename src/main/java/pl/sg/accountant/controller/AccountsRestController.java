package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.Account;
import pl.sg.accountant.model.FinancialTransaction;
import pl.sg.accountant.service.AccountsService;
import pl.sg.accountant.service.AccountstException;
import pl.sg.accountant.transport.AccountTO;
import pl.sg.accountant.transport.TransactionTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.security.AuthorizationService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
public class AccountsRestController implements AccountsController {
    private final AccountsService accountsService;
    private final ModelMapper mapper;
    private final AuthorizationService authorizationService;

    public AccountsRestController(AccountsService accountsService, ModelMapper mapper, AuthorizationService authorizationService) {
        this.accountsService = accountsService;
        this.mapper = mapper;
        this.authorizationService = authorizationService;
    }

    @Override
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<AccountTO>> accounts(AccountTO account, @RequestHeader("Authorization") String token) {
        authorizationService.validate(token, "ADMIN");
        return ResponseEntity.ok(accountsService.getAll().stream().map(a -> mapper.map(a, AccountTO.class)).collect(Collectors.toList()));
    }

    @Override
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<AccountTO> createAccount(@RequestBody AccountTO account, @RequestHeader("Authorization") String token) {
        ApplicationUser user = authorizationService.validate(token, "ADMIN");
        Account toCreate = mapper.map(account, Account.class);
        accountsService.createAccount(toCreate, user.getLogin());
        return ResponseEntity.ok(mapper.map(toCreate, AccountTO.class));
    }

    @Override
    @RequestMapping(value = "/transfer/{from}/{to}/{amount}", method = RequestMethod.POST)
    public ResponseEntity<TransactionTO> transfer(
            @PathVariable("from") int fromId,
            @PathVariable("to") int toId,
            @PathVariable("amount") BigDecimal amount,
            @RequestBody String description,
            @RequestHeader("Authorization") String token) throws AccountstException {
        ApplicationUser user = authorizationService.validate(token, "ADMIN");
        FinancialTransaction result = accountsService.transferMoneyWithoutConversion(
                fromId,
                toId,
                amount,
                description,
                user.getLogin());
        return ResponseEntity.ok(mapper.map(result, TransactionTO.class));
    }
}
