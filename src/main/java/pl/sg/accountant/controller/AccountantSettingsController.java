package pl.sg.accountant.controller;

import pl.sg.accountant.transport.AccountantSettingsTO;
import pl.sg.application.model.Domain;

public interface AccountantSettingsController {

    AccountantSettingsTO getForDomain(Domain domain);

    AccountantSettingsTO enableIsCompany(Domain domain);

    AccountantSettingsTO disableIsCompany(Domain domain);
}
