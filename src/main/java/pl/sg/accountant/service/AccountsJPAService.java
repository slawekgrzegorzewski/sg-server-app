package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.Account;
import pl.sg.accountant.model.FinancialTransaction;
import pl.sg.accountant.repository.AccountRepository;
import pl.sg.accountant.repository.FinancialTransactionRepository;
import pl.sg.application.model.ApplicationUserRepository;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AccountsJPAService implements AccountsService {
    private final AccountRepository accountRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final ApplicationUserRepository applicationUserRepository;

    public AccountsJPAService(
            AccountRepository accountRepository,
            FinancialTransactionRepository financialTransactionRepository,
            ApplicationUserRepository applicationUserRepository) {
        this.accountRepository = accountRepository;
        this.financialTransactionRepository = financialTransactionRepository;
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public void createAccount(Account account, String userName) {
        account.setApplicationUser(applicationUserRepository.findFirstByLogin(userName).get());
        accountRepository.save(account);
    }

    @Override
    public FinancialTransaction transferMoneyWithoutConversion(
            int sourceAccount,
            int destinationAccount,
            BigDecimal amount,
            String description,
            String userName) throws AccountstException {
        Account from = accountRepository.getOne(sourceAccount);
        Account to = accountRepository.getOne(destinationAccount);
        if (!from.getCurrency().equals(to.getCurrency())) {
            throw new AccountstException("Accounts currencies differ: source is " + from.getCurrency().getCurrencyCode() + ", target is " + to.getCurrency().getCurrencyCode());
        }
        if (from.getCurrentBalance().compareTo(amount) < 0) {
            throw new AccountstException("No enough money on source account");
        }
        FinancialTransaction financialTransaction = new FinancialTransaction();
        financialTransaction.setDescription(description)
                .setSource(from)
                .setDestination(to)
                .setCredit(amount)
                .setDebit(amount)
                .setApplicationUser(applicationUserRepository.findFirstByLogin(userName).get());
        financialTransactionRepository.save(financialTransaction);
        from.setBalanceIndex(financialTransaction.getId())
                .setCurrentBalance(from.getCurrentBalance().subtract(amount));
        to.setBalanceIndex(financialTransaction.getId())
                .setCurrentBalance(to.getCurrentBalance().add(amount));
        accountRepository.saveAll(List.of(from, to));
        return financialTransaction;
    }
}
