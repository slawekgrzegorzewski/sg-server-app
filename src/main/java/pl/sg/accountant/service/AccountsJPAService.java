package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.repository.AccountRepository;
import pl.sg.application.model.Domain;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AccountsJPAService implements AccountsService {
    private final AccountRepository accountRepository;

    public AccountsJPAService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getById(Integer accountId) {
        return accountRepository.getOne(accountId);
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> getForDomain(Domain domain) {
        return accountRepository.findByDomain(domain);
    }

    @Override
    public void createAccount(Account account) {
        validateCreditLimit(account);
        account.setId(null);
        accountRepository.save(account);
    }

    @Override
    public void update(Account toEdit) {
        validateCreditLimit(toEdit);
        accountRepository.save(toEdit);
    }

    private void validateCreditLimit(Account account) {
        if (account.getAvailableBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new AccountsException("Credit limit too low");
        }
    }

    @Override
    public void delete(Account toDelete) {
        accountRepository.delete(toDelete);
    }
}
