package pl.sg.integrations.nodrigen.controller;

import pl.sg.application.model.Domain;
import pl.sg.integrations.nodrigen.model.rest.NodrigenInstitution;
import pl.sg.integrations.nodrigen.transport.NodrigenBankPermission;
import pl.sg.integrations.nodrigen.transport.NodrigenPermissionRequest;
import pl.sg.integrations.nodrigen.transport.NodrigenTransactionsToImportTO;

import java.util.List;

public interface NodrigenController {

    List<NodrigenInstitution> getInstitutions(String country);

    List<NodrigenInstitution> getInstitutionsToRecreate(Domain domain);

    List<NodrigenBankPermission> getPermissionsGranted(Domain domain);

    List<NodrigenBankPermission> getPermissionToProceed(Domain domain);

    String confirmPermission(Domain domain, String reference);

    List<NodrigenBankPermission> getPermissionToProceed(Domain domain, NodrigenPermissionRequest nodrigenPermissionRequest);

    List<NodrigenTransactionsToImportTO> getNodrigenTransactionsToImport(Domain domain);

    void mutuallyCancelTransactions(Domain domain, int firstTransactionId, int secondTransactionId);

    void ignoreTransactions(Domain domain, List<Integer> transactionsId);

    void fetch(Domain domain, String bankAccountExternalId);
}