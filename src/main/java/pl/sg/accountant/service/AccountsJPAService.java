package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.repository.AccountRepository;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;
import pl.sg.application.service.DomainService;

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
        account.setId(null);
        accountRepository.save(account);
    }

    @Override
    public void update(Account toEdit) {
        accountRepository.save(toEdit);
    }

    @Override
    public void delete(Account toDelete) {
        accountRepository.delete(toDelete);
    }
}
