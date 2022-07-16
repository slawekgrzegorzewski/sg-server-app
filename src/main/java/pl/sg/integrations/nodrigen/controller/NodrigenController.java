package pl.sg.integrations.nodrigen.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import pl.sg.accountant.model.accounts.FinancialTransaction;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.integrations.nodrigen.model.transcations.NodrigenTransaction;
import pl.sg.integrations.nodrigen.transport.NodrigenBankPermissionTO;
import pl.sg.integrations.nodrigen.transport.NodrigenPermissionRequest;
import pl.sg.integrations.nodrigen.transport.NodrigenTransactionsToImportTO;

import java.util.List;

public interface NodrigenController {

    ResponseEntity<String> getInstitution(String country);

    List<NodrigenBankPermissionTO> getPermissionsGranted(Domain domain);

    List<NodrigenBankPermissionTO> getPermissionToProceed(Domain domain);

    String confirmPermission(Domain domain, String reference);

    List<NodrigenBankPermissionTO> getPermissionToProceed(Domain domain, NodrigenPermissionRequest nodrigenPermissionRequest);

    List<NodrigenTransactionsToImportTO> getNodrigenTransactionsToImport(Domain domain);
    void mutuallyCancelTransactions(Domain domain, int firstTransactionId, int secondTransactionId);

    void fetch(Domain domain, String bankAccountExternalId);
}