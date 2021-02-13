package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.accounts.FinancialTransaction;
import pl.sg.accountant.service.TransactionsService;
import pl.sg.accountant.transport.FinancialTransactionTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.security.annotations.RequestUser;
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
    @GetMapping("/{domainId}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<FinancialTransactionTO> getUserTransactions(@RequestUser ApplicationUser user, @PathVariable int domainId) {
        return transactionsService.transactionsForUserAndDomain(user, domainId).stream()
                .map(t -> this.mapper.map(t, FinancialTransactionTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PostMapping("/transfer/{from}/{to}/{amount}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialTransactionTO transfer(@RequestUser ApplicationUser user,
                                           @PathVariable("from") int fromId,
                                           @PathVariable("to") int toId,
                                           @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                           @RequestBody String description) {
        FinancialTransaction result = transactionsService.transferMoneyWithoutConversion(fromId, toId, amount, description, user);
        return mapper.map(result, FinancialTransactionTO.class);
    }

    @Override
    @PostMapping("/transfer_with_conversion/{from}/{to}/{amount}/{targetAmount}/{rate}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialTransactionTO transferWithConversion(@RequestUser ApplicationUser user,
                                                         @PathVariable("from") int fromId,
                                                         @PathVariable("to") int toId,
                                                         @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                                         @PathVariable("targetAmount") @PositiveOrZero BigDecimal targetAmount,
                                                         @PathVariable("rate") @Positive BigDecimal rate,
                                                         @RequestBody String description) {
        FinancialTransaction result = transactionsService.transferMoneyWithConversion(fromId, toId, amount, targetAmount, rate, description, user);
        return mapper.map(result, FinancialTransactionTO.class);
    }

    @Override
    @PostMapping("/credit/{accountId}/{amount}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialTransactionTO credit(@RequestUser ApplicationUser user,
                                         @PathVariable int accountId,
                                         @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                         @RequestBody String description) {
        FinancialTransaction result = transactionsService.credit(accountId, amount, description, user);
        return mapper.map(result, FinancialTransactionTO.class);
    }

    @Override
    @PostMapping("/debit/{accountId}/{amount}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialTransactionTO debit(@RequestUser ApplicationUser user,
                                        @PathVariable int accountId,
                                        @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                        @RequestBody String description) {
        FinancialTransaction result = transactionsService.debit(accountId, amount, description, user);
        return mapper.map(result, FinancialTransactionTO.class);
    }
}
