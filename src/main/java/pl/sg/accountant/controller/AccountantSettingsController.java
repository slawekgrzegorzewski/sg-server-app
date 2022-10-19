package pl.sg.accountant.controller;

import pl.sg.accountant.transport.AccountantSettings;
import pl.sg.application.model.Domain;

public interface AccountantSettingsController {

    AccountantSettings getForDomain(Domain domain);

    AccountantSettings enableIsCompany(Domain domain);

    AccountantSettings disableIsCompany(Domain domain);
}
