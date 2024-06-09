package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.AccountantSettings;
import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.repository.AccountantSettingsRepository;
import pl.sg.application.model.Domain;
import pl.sg.application.repository.DomainRepository;

@Component
public class AccountantSettingsJPAService implements AccountantSettingsService {
    private final AccountantSettingsRepository accountantSettingsRepository;
    private final DomainRepository domainRepository;

    public AccountantSettingsJPAService(AccountantSettingsRepository accountantSettingsRepository, DomainRepository domainRepository) {
        this.accountantSettingsRepository = accountantSettingsRepository;
        this.domainRepository = domainRepository;
    }

    @Override
    public AccountantSettings getForDomain(int domainId) {
        Domain domain = domainRepository.getReferenceById(domainId);
        if (accountantSettingsRepository.findByDomain(domain).isEmpty()) {
            createForDomain(domain);
        }
        return accountantSettingsRepository.getByDomain(domain);
    }

    @Override
    public AccountantSettings createForDomain(Domain domain) {
        if (accountantSettingsRepository.findByDomain(domain).isPresent()) {
            throw new AccountsException("Accountant settings for domain " + domain.getId() + " already exists");
        }
        return accountantSettingsRepository.save(
                new AccountantSettings().setDomain(domain)
        );
    }

    @Override
    public AccountantSettings enableIsCompany(Domain domain) {
        return accountantSettingsRepository.save(
                accountantSettingsRepository.getByDomain(domain).setCompany(true)
        );
    }

    @Override
    public AccountantSettings disableIsCompany(Domain domain) {
        return accountantSettingsRepository.save(
                accountantSettingsRepository.getByDomain(domain).setCompany(false)
        );
    }
}
