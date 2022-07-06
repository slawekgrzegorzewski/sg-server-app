package pl.sg.integrations.nodrigen.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.integrations.nodrigen.repository.NodrigenTransactionsToImportRepository;
import pl.sg.integrations.nodrigen.services.NodrigenService;
import pl.sg.integrations.nodrigen.NodrigenClient;
import pl.sg.integrations.nodrigen.model.NodrigenBankPermission;
import pl.sg.integrations.nodrigen.repository.NodrigenBankPermissionRepository;
import pl.sg.integrations.nodrigen.transport.NodrigenBankPermissionTO;
import pl.sg.integrations.nodrigen.transport.NodrigenPermissionRequest;
import pl.sg.integrations.nodrigen.transport.NodrigenTransactionsToImportTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/nodrigen")
public class NodrigenControllerImpl implements NodrigenController {

    private final ModelMapper modelMapper;
    private final NodrigenClient nodrigenClient;
    private final NodrigenBankPermissionRepository nodrigenBankPermissionRepository;
    private final NodrigenService nodrigenService;
    private final NodrigenTransactionsToImportRepository nodrigenTransactionsToImportRepository;

    public NodrigenControllerImpl(ModelMapper modelMapper, NodrigenClient nodrigenClient,
                                  NodrigenBankPermissionRepository nodrigenBankPermissionRepository,
                                  NodrigenService nodrigenService,
                                  NodrigenTransactionsToImportRepository nodrigenTransactionsToImportRepository) {
        this.modelMapper = modelMapper;
        this.nodrigenClient = nodrigenClient;
        this.nodrigenBankPermissionRepository = nodrigenBankPermissionRepository;
        this.nodrigenService = nodrigenService;
        this.nodrigenTransactionsToImportRepository = nodrigenTransactionsToImportRepository;
    }

    @Override
    @GetMapping("/institutions/{country}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ResponseEntity<String> getInstitution(@PathVariable String country) {
        return nodrigenClient.listInstitutions(country);
    }

    @Override
    @GetMapping("/permissions/granted")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<NodrigenBankPermissionTO> getPermissionsGranted(@RequestDomain Domain domain) {
        return nodrigenBankPermissionRepository.findPermissionsGranted(domain)
                .stream()
                .map(nbp -> this.modelMapper.map(nbp, NodrigenBankPermissionTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @GetMapping("/permissions/to_proccess")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<NodrigenBankPermissionTO> getPermissionToProceed(@RequestDomain Domain domain) {
        return nodrigenBankPermissionRepository.findPermissionsToConfirm(domain)
                .stream()
                .map(nbp -> this.modelMapper.map(nbp, NodrigenBankPermissionTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping("/permissions")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String confirmPermission(@RequestDomain Domain domain, @RequestBody String reference) {
        nodrigenService.confirmPermission(domain, reference);
        return "OK";
    }

    @Override
    @PostMapping("/permissions")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<NodrigenBankPermissionTO> getPermissionToProceed(
            @RequestDomain Domain domain,
            @RequestBody NodrigenPermissionRequest nodrigenPermissionRequest) {
        NodrigenBankPermission permission = nodrigenService.createRequisition(domain, nodrigenPermissionRequest);
        nodrigenBankPermissionRepository.save(permission);
        return nodrigenBankPermissionRepository.findPermissionsToConfirm(domain)
                .stream()
                .map(nbp -> this.modelMapper.map(nbp, NodrigenBankPermissionTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @GetMapping("/nodrigen_transaction_to_import")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<NodrigenTransactionsToImportTO> getNodrigenTransactionsToImport(@RequestDomain Domain domain) {
        return nodrigenTransactionsToImportRepository.findNodrigenTransactionsToImportByDomainId(domain.getId())
                .stream()
                .map(t -> modelMapper.map(t, NodrigenTransactionsToImportTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping("/nodrigen_transaction_to_import/{nodrigenTransactionId}/{financialTransactionId}/{matchingMode}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<NodrigenTransactionsToImportTO> matchNodrigenTransactionsToImport(
            @RequestDomain Domain domain,
            @PathVariable int nodrigenTransactionId,
            @PathVariable int financialTransactionId,
            @PathVariable MatchingMode matchingMode) {
        return nodrigenService.matchNodrigenTransactionsToImport(domain, nodrigenTransactionId, financialTransactionId, matchingMode);
    }

    @Override
    @PutMapping("/nodrigen_transactions_to_import/{nodrigenTransactionId}/{secondNodrigenTransactionId}/{financialTransactionId}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<NodrigenTransactionsToImportTO> matchNodrigenTransactionsToImport(
            @RequestDomain Domain domain,
            @PathVariable int nodrigenTransactionId,
            @PathVariable int secondNodrigenTransactionId,
            @PathVariable int financialTransactionId) {
        return nodrigenService.matchNodrigenTransactionsToImport(domain, nodrigenTransactionId, secondNodrigenTransactionId, financialTransactionId);
    }

    @Override
    @PutMapping("/nodrigen_transactions_to_import/{nodrigenTransactionId}/{financialTransactionId}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<NodrigenTransactionsToImportTO> matchNodrigenTransactionsToImport(
            @RequestDomain Domain domain,
            @PathVariable int nodrigenTransactionId,
            @PathVariable int financialTransactionId) {
        return nodrigenService.matchNodrigenTransactionsToImport(domain, nodrigenTransactionId, financialTransactionId);
    }
}