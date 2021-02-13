package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.repository.AccountRepository;
import pl.sg.application.model.ApplicationUser;

import java.util.List;

@Component
public class AccountsJPAService implements AccountsService {
    private final AccountRepository accountRepository;

    public AccountsJPAService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getById(ApplicationUser user, Integer accountId) {
        final Account result = accountRepository.getOne(accountId);
        user.validateDomain(result.getDomain());
        return result;
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> getForUserAndDomain(ApplicationUser user, int domainId) {
        return accountRepository.findAllByApplicationUserLoginAndDomain(user, domainId);
    }

    @Override
    public void createAccount(ApplicationUser user, Account account) {
        user.validateAdminDomain(account.getDomain());
        accountRepository.save(account);
    }

    @Override
    public void update(ApplicationUser user, Account toEdit) {
        final Account dbValue = getById(user, toEdit.getId());
        user.validateDomain(dbValue.getDomain());
        accountRepository.save(dbValue);
    }

    @Override
    public void delete(ApplicationUser user, int accountId) {
        final Account toDelete = getById(user, accountId);
        user.validateAdminDomain(toDelete.getDomain());
        accountRepository.delete(toDelete);
    }
}
