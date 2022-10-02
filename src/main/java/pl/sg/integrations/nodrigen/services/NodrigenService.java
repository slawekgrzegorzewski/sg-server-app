package pl.sg.integrations.nodrigen.services;

import pl.sg.application.model.Domain;
import pl.sg.banks.model.BankAccount;
import pl.sg.integrations.nodrigen.model.NodrigenBankPermission;
import pl.sg.integrations.nodrigen.transport.NodrigenPermissionRequest;

import java.util.List;

public interface NodrigenService {

    NodrigenBankPermission createRequisition(Domain domain, NodrigenPermissionRequest nodrigenPermissionRequest);

    void recreateRequisitionIfNeeded(BankAccount bankAccount);

    void confirmPermission(Domain domain, String reference);

    void mutuallyCancelTransactions(Domain domain, int firstTransactionId, int secondTransactionId);

    void ignoreTransactions(Domain domain, int transactionId);

    List<String> getInstitutionsToRecreate(Domain domain);
}