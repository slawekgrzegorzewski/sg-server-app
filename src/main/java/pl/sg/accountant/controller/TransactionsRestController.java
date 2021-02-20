package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.accounts.FinancialTransaction;
import pl.sg.accountant.service.TransactionsService;
import pl.sg.accountant.transport.FinancialTransactionTO;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.PathVariableWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import javax.validation.constraints.Positive;
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
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<FinancialTransactionTO> getUserTransactions(@RequestDomain Domain domain) {
        return transactionsService.transactionsForDomain(domain).stream()
                .map(t -> this.mapper.map(t, FinancialTransactionTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PostMapping("/transfer/{from}/{to}/{amount}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialTransactionTO transfer(@PathVariableWithDomain Account from,
                                           @PathVariableWithDomain Account to,
                                           @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                           @RequestBody String description) {
        FinancialTransaction result = transactionsService.transferMoneyWithoutConversion(from, to, amount, description);
        return mapper.map(result, FinancialTransactionTO.class);
    }

    @Override
    @PostMapping("/transfer_with_conversion/{from}/{to}/{amount}/{targetAmount}/{rate}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialTransactionTO transferWithConversion(@PathVariableWithDomain Account from,
                                                         @PathVariableWithDomain Account to,
                                                         @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                                         @PathVariable("targetAmount") @PositiveOrZero BigDecimal targetAmount,
                                                         @PathVariable("rate") @Positive BigDecimal rate,
                                                         @RequestBody String description) {
        FinancialTransaction result = transactionsService.transferMoneyWithConversion(from, to, amount, targetAmount, rate, description);
        return mapper.map(result, FinancialTransactionTO.class);
    }

    @Override
    @PostMapping("/credit/{account}/{amount}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialTransactionTO credit(@PathVariableWithDomain Account account,
                                         @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                         @RequestBody String description) {
        FinancialTransaction result = transactionsService.credit(account, amount, description);
        return mapper.map(result, FinancialTransactionTO.class);
    }

    @Override
    @PostMapping("/debit/{account}/{amount}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialTransactionTO debit(@PathVariableWithDomain Account account,
                                        @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                        @RequestBody String description) {
        FinancialTransaction result = transactionsService.debit(account, amount, description);
        return mapper.map(result, FinancialTransactionTO.class);
    }
}
