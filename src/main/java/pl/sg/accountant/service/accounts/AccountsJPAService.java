package pl.sg.accountant.service.accounts;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.repository.accounts.AccountRepository;
import pl.sg.application.repository.DomainRepository;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AccountsJPAService implements AccountsService {
    private final AccountRepository accountRepository;
    private final DomainRepository domainRepository;

    public AccountsJPAService(AccountRepository accountRepository, DomainRepository domainRepository) {
        this.accountRepository = accountRepository;
        this.domainRepository = domainRepository;
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
    public List<Account> getForDomain(long domainId) {
        return accountRepository.findByDomain(domainRepository.getReferenceById((int) domainId));
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
