package pl.sg.banks.integrations.nodrigen.controller;

import org.springframework.http.ResponseEntity;
import pl.sg.application.model.Domain;
import pl.sg.banks.integrations.nodrigen.transport.NodrigenBankPermissionTO;
import pl.sg.banks.integrations.nodrigen.transport.NodrigenPermissionRequest;

import java.net.URISyntaxException;
import java.util.List;

public interface NodrigenController {

    ResponseEntity<String> getInstitution(String country);

    List<NodrigenBankPermissionTO> getPermissionsGranted(Domain domain);

    List<NodrigenBankPermissionTO> getPermissionToProceed(Domain domain);

    String confirmPermission(Domain domain, String reference);

    List<NodrigenBankPermissionTO> getPermissionToProceed(Domain domain, NodrigenPermissionRequest nodrigenPermissionRequest);
}
