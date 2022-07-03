package pl.sg.integrations.nodrigen.services;

import pl.sg.application.model.Domain;
import pl.sg.integrations.nodrigen.model.NodrigenBankPermission;
import pl.sg.integrations.nodrigen.transport.NodrigenPermissionRequest;

public interface NodrigenService {

    NodrigenBankPermission createRequisition(Domain domain, NodrigenPermissionRequest nodrigenPermissionRequest);
    void confirmPermission(Domain domain, String reference);
}
