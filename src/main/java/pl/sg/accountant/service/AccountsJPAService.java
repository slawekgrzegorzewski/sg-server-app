package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.repository.AccountRepository;
import pl.sg.application.model.ApplicationUserRepository;

import java.util.List;
import java.util.Optional;

@Component
public class AccountsJPAService implements AccountsService {
    private final AccountRepository accountRepository;
    private final ApplicationUserRepository applicationUserRepository;

    public AccountsJPAService(
            AccountRepository accountRepository,
            ApplicationUserRepository applicationUserRepository) {
        this.accountRepository = accountRepository;
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public Optional<Account> findById(Integer id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account getById(Integer id) {
        return accountRepository.getOne(id);
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> getForUser(String userName) {
        return accountRepository.findAllByApplicationUserLogin(userName);
    }

    @Override
    public void createAccount(Account account, String userName) {
        account.setApplicationUser(applicationUserRepository.findFirstByUserLogins(userName).get());
        accountRepository.save(account);
    }

    @Override
    public void update(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void delete(Account account) {
        accountRepository.delete(account);
    }
}
