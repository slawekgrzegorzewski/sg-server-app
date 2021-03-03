package pl.sg.accountant.service;

import pl.sg.accountant.model.AccountantSettings;
import pl.sg.application.model.Domain;

public interface AccountantSettingsService {

    AccountantSettings getForDomain(Domain domain);

    AccountantSettings createForDomain(Domain domain);

    AccountantSettings enableIsCompany(Domain domain);

    AccountantSettings disableIsCompany(Domain domain);
}
