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

import java.math.BigDecimal;

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
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<AccountTO> createAccount(@RequestBody AccountTO account) {
        Account toCreate = mapper.map(account, Account.class);
        accountsService.createAccount(toCreate);
        return ResponseEntity.ok(mapper.map(toCreate, AccountTO.class));
    }

    @Override
    @RequestMapping(value = "/transfer/{from}/{to}/{amount}", method = RequestMethod.POST)
    public ResponseEntity<TransactionTO> transfer(
            @PathVariable("from") int fromId,
            @PathVariable("to") int toId,
            @PathVariable("amount") BigDecimal amount,
            @RequestBody String description) throws AccountstException {
        FinancialTransaction result = accountsService.transferMoneyWithoutConversion(fromId, toId, amount, description);
        return ResponseEntity.ok(mapper.map(result, TransactionTO.class));
    }
}
