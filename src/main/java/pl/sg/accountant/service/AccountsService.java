package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.Account;
import pl.sg.accountant.model.FinancialTransaction;
import pl.sg.accountant.repository.AccountRepository;
import pl.sg.accountant.repository.FinancialTransactionRepository;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AccountsService {
    private final AccountRepository accountRepository;
    private final FinancialTransactionRepository financialTransactionRepository;

    public AccountsService(AccountRepository accountRepository, FinancialTransactionRepository financialTransactionRepository) {
        this.accountRepository = accountRepository;
        this.financialTransactionRepository = financialTransactionRepository;
    }

    public void createAccount(Account account) {
        accountRepository.save(account);
    }

    public FinancialTransaction transferMoneyWithoutConversion(int sourceAccount, int destinationAccount, BigDecimal amount, String description) throws AccountstException {
        Account from = accountRepository.getOne(sourceAccount);
        Account to = accountRepository.getOne(destinationAccount);
        if (!from.getCurrency().equals(to.getCurrency())) {
            throw new AccountstException("Accounts currencies differ: source is " + from.getCurrency().getCurrencyCode() + ", target is " + to.getCurrency().getCurrencyCode());
        }
        if (from.getCurrentBalance().compareTo(amount) < 0) {
            throw new AccountstException("No enaugh money on source account");
        }
        FinancialTransaction financialTransaction = new FinancialTransaction();
        financialTransaction.setDescription(description)
                .setSource(from)
                .setDestination(to)
                .setCredit(amount)
                .setDebit(amount);
        financialTransactionRepository.save(financialTransaction);
        from.setBalanceIndex(financialTransaction.getId())
                .setCurrentBalance(from.getCurrentBalance().subtract(amount));
        to.setBalanceIndex(financialTransaction.getId())
                .setCurrentBalance(to.getCurrentBalance().add(amount));
        accountRepository.saveAll(List.of(from, to));
        return financialTransaction;
    }
}
