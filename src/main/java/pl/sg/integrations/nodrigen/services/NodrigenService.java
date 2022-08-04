package pl.sg.integrations.nodrigen.services;

import pl.sg.accountant.model.accounts.FinancialTransaction;
import pl.sg.application.model.Domain;
import pl.sg.integrations.nodrigen.controller.MatchingMode;
import pl.sg.integrations.nodrigen.model.NodrigenBankPermission;
import pl.sg.integrations.nodrigen.model.transcations.NodrigenTransaction;
import pl.sg.integrations.nodrigen.transport.NodrigenPermissionRequest;
import pl.sg.integrations.nodrigen.transport.NodrigenTransactionsToImportTO;

import java.util.List;

public interface NodrigenService {

    NodrigenBankPermission createRequisition(Domain domain, NodrigenPermissionRequest nodrigenPermissionRequest);

    void confirmPermission(Domain domain, String reference);

    void mutuallyCancelTransactions(Domain domain, int firstTransactionId, int secondTransactionId);
    void ignoreTransactions(Domain domain, int transactionId);

    void fetch(Domain domain, String bankAccountExternalId);
}