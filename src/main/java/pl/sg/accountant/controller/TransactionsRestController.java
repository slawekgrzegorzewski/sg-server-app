package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.FinancialTransaction;
import pl.sg.accountant.service.AccountsException;
import pl.sg.accountant.service.TransactionsService;
import pl.sg.accountant.transport.FinancialTransactionTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.security.annotations.RequestUser;
import pl.sg.application.security.annotations.TokenBearerAuth;

import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transactions")
@Validated
public class TransactionsRestController implements TransactionsController {

    private final TransactionsService transactionsService;
    private final ModelMapper mapper;

    public TransactionsRestController(TransactionsService transactionsService, ModelMapper mapper) {
        this.transactionsService = transactionsService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public List<FinancialTransactionTO> getUserTransactions(@RequestUser(RequestUser.LOGIN) String login) {
        return transactionsService.transactionsForUser(login).stream()
                .map(t -> this.mapper.map(t, FinancialTransactionTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PostMapping("/{from}/{to}/{amount}")
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public FinancialTransactionTO transfer(
            @PathVariable("from") int fromId,
            @PathVariable("to") int toId,
            @PathVariable("amount") @PositiveOrZero BigDecimal amount,
            @RequestBody String description,
            @RequestUser ApplicationUser user) throws AccountsException {
        FinancialTransaction result = transactionsService.transferMoneyWithoutConversion(fromId, toId, amount, description, user);
        return mapper.map(result, FinancialTransactionTO.class);
    }

    @Override
    @PostMapping("/credit/{accountId}/{amount}")
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public FinancialTransactionTO credit(
            @PathVariable int accountId,
            @PathVariable("amount") @PositiveOrZero BigDecimal amount,
            @RequestBody String description,
            @RequestUser ApplicationUser user) throws AccountsException {
        FinancialTransaction result = transactionsService.credit(accountId, amount, description, user);
        return mapper.map(result, FinancialTransactionTO.class);
    }

    @Override
    @PostMapping("/debit/{accountId}/{amount}")
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public FinancialTransactionTO debit(
            @PathVariable int accountId,
            @PathVariable("amount") @PositiveOrZero BigDecimal amount,
            @RequestBody String description,
            @RequestUser ApplicationUser user) throws AccountsException {
        FinancialTransaction result = transactionsService.debit(accountId, amount, description, user);
        return mapper.map(result, FinancialTransactionTO.class);
    }
}
