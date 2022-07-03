package pl.sg.integrations.nodrigen.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.integrations.nodrigen.services.NodrigenService;
import pl.sg.integrations.nodrigen.NodrigenClient;
import pl.sg.integrations.nodrigen.model.NodrigenBankPermission;
import pl.sg.integrations.nodrigen.repository.NodrigenBankPermissionRepository;
import pl.sg.integrations.nodrigen.transport.NodrigenBankPermissionTO;
import pl.sg.integrations.nodrigen.transport.NodrigenPermissionRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/nodrigen")
public class NodrigenControllerImpl implements NodrigenController {

    private final ModelMapper modelMapper;
    private final NodrigenClient nodrigenClient;
    private final NodrigenBankPermissionRepository nodrigenBankPermissionRepository;
    private final NodrigenService nodrigenService;

    public NodrigenControllerImpl(ModelMapper modelMapper, NodrigenClient nodrigenClient, NodrigenBankPermissionRepository nodrigenBankPermissionRepository, NodrigenService nodrigenService) {
        this.modelMapper = modelMapper;
        this.nodrigenClient = nodrigenClient;
        this.nodrigenBankPermissionRepository = nodrigenBankPermissionRepository;
        this.nodrigenService = nodrigenService;
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
}