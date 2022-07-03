package pl.sg.banks.integrations.nodrigen.services;

import pl.sg.application.model.Domain;
import pl.sg.banks.integrations.nodrigen.model.NodrigenBankPermission;
import pl.sg.banks.integrations.nodrigen.transport.NodrigenPermissionRequest;

import java.net.URISyntaxException;

public interface NodrigenService {

    NodrigenBankPermission createRequisition(Domain domain, NodrigenPermissionRequest nodrigenPermissionRequest);
    void confirmPermission(Domain domain, String reference);
}
