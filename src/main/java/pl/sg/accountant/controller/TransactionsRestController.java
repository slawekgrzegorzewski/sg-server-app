package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.ledger.FinancialTransaction;
import pl.sg.accountant.service.ledger.TransactionsService;
import pl.sg.accountant.transport.FinancialTransactionTO;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.PathVariableWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
    public FinancialTransactionTO transfer(@PathVariableWithDomain("from") Account from,
                                           @PathVariableWithDomain("to") Account to,
                                           @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                           @RequestBody String description) {
        FinancialTransaction result = transactionsService.transferMoneyWithoutConversion(from, to, amount, description);
        return mapper.map(result, FinancialTransactionTO.class);
    }

    @Override
    @PostMapping("/transfer_cash/{from}/{to}/{amount}/{firstBankTransactionId}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialTransactionTO transferCashWithBankTransactions(@PathVariableWithDomain("from") Account from,
                                                                   @PathVariableWithDomain("to") Account to,
                                                                   @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                                                   @RequestBody String description,
                                                                   @PathVariable("firstBankTransactionId") int firstBankTransactionId) {
        FinancialTransaction result = transactionsService.transferCashWithoutConversionWithBankTransactions(from, to,
                amount, description, firstBankTransactionId);
        return mapper.map(result, FinancialTransactionTO.class);
    }

    @Override
    @PostMapping("/transfer/{from}/{to}/{amount}/{firstBankTransactionId}/{secondBankTransactionId}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialTransactionTO transferWithBankTransactions(@PathVariableWithDomain("from") Account from,
                                                               @PathVariableWithDomain("to") Account to,
                                                               @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                                               @RequestBody String description,
                                                               @PathVariable("firstBankTransactionId") int firstBankTransactionId,
                                                               @PathVariable("secondBankTransactionId") int secondBankTransactionId) {
        FinancialTransaction result = transactionsService.transferMoneyWithoutConversionWithBankTransactions(from, to,
                amount, description, firstBankTransactionId, secondBankTransactionId);
        return mapper.map(result, FinancialTransactionTO.class);
    }

    @Override
    @PostMapping("/transfer_with_conversion/{from}/{to}/{amount}/{targetAmount}/{rate}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialTransactionTO transferWithConversion(@PathVariableWithDomain("from") Account from,
                                                         @PathVariableWithDomain("to") Account to,
                                                         @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                                         @PathVariable("targetAmount") @PositiveOrZero BigDecimal targetAmount,
                                                         @PathVariable("rate") @Positive BigDecimal rate,
                                                         @RequestBody String description) {
        FinancialTransaction result = transactionsService.transferMoneyWithConversion(from, to, amount, targetAmount, rate, description);
        return mapper.map(result, FinancialTransactionTO.class);
    }

    @Override
    @PostMapping("/transfer_cash_with_conversion/{from}/{to}/{amount}/{targetAmount}/{rate}/{firstBankTransactionId}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialTransactionTO transferCashWithConversionWithBankTransactions(@PathVariableWithDomain("from") Account from,
                                                                             @PathVariableWithDomain("to") Account to,
                                                                             @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                                                             @PathVariable("targetAmount") @PositiveOrZero BigDecimal targetAmount,
                                                                             @PathVariable("rate") @Positive BigDecimal rate,
                                                                             @RequestBody String description,
                                                                             @PathVariable("firstBankTransactionId") int firstBankTransactionId) {
        FinancialTransaction result = transactionsService.transferCashWithConversionWithBankTransactions(from, to, amount,
                targetAmount, rate, description, firstBankTransactionId);
        return mapper.map(result, FinancialTransactionTO.class);
    }

    @Override
    @PostMapping("/transfer_with_conversion/{from}/{to}/{amount}/{targetAmount}/{rate}/{firstBankTransactionId}/{secondBankTransactionId}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialTransactionTO transferWithConversionWithBankTransactions(@PathVariableWithDomain("from") Account from,
                                                                             @PathVariableWithDomain("to") Account to,
                                                                             @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                                                             @PathVariable("targetAmount") @PositiveOrZero BigDecimal targetAmount,
                                                                             @PathVariable("rate") @Positive BigDecimal rate,
                                                                             @RequestBody String description,
                                                                             @PathVariable("firstBankTransactionId") int firstBankTransactionId,
                                                                             @PathVariable("secondBankTransactionId") int secondBankTransactionId) {
        FinancialTransaction result = transactionsService.transferMoneyWithConversionWithBankTransactions(from, to, amount,
                targetAmount, rate, description, firstBankTransactionId, secondBankTransactionId);
        return mapper.map(result, FinancialTransactionTO.class);
    }

    @Override
    @PostMapping("/credit/{account}/{amount}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialTransactionTO credit(@PathVariableWithDomain("account") Account account,
                                         @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                         @RequestBody String description) {
        FinancialTransaction result = transactionsService.credit(account, amount, description);
        return mapper.map(result, FinancialTransactionTO.class);
    }

    @Override
    @PostMapping("/debit/{account}/{amount}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialTransactionTO debit(@PathVariableWithDomain("account") Account account,
                                        @PathVariable("amount") @PositiveOrZero BigDecimal amount,
                                        @RequestBody String description) {
        FinancialTransaction result = transactionsService.debit(account, amount, description);
        return mapper.map(result, FinancialTransactionTO.class);
    }
}
