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
import pl.sg.application.security.annotations.RequestUser;
import pl.sg.application.security.annotations.TokenBearerAuth;

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
    @TokenBearerAuth("ADMIN")
    public ResponseEntity<List<AccountTO>> accounts(AccountTO account) {
        return ResponseEntity.ok(accountsService.getAll().stream().map(a -> mapper.map(a, AccountTO.class)).collect(Collectors.toList()));
    }

    @Override
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @TokenBearerAuth("ADMIN")
    public ResponseEntity<AccountTO> createAccount(
            @RequestBody AccountTO account,
            @RequestUser(RequestUser.LOGIN) String login) {
        Account toCreate = mapper.map(account, Account.class);
        accountsService.createAccount(toCreate, login);
        return ResponseEntity.ok(mapper.map(toCreate, AccountTO.class));
    }

    @Override
    @RequestMapping(value = "/transfer/{from}/{to}/{amount}", method = RequestMethod.POST)
    @TokenBearerAuth("ADMIN")
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
