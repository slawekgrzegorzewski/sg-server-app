package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.service.PerformedServicesService;
import pl.sg.accountant.transport.accounts.PerformedService;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import jakarta.validation.Valid;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import static pl.sg.Application.CREATE_PERFORMED_SERVICE;
import static pl.sg.Application.UPDATE_PERFORMED_SERVICE;

@RestController
@RequestMapping("/performed-services")
@Validated
public class PerformedServicesRestController implements PerformedServicesController {
    private final PerformedServicesService performedServicesService;
    private final ModelMapper mapper;

    public PerformedServicesRestController(PerformedServicesService performedServicesService, ModelMapper mapper) {
        this.performedServicesService = performedServicesService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping("/{forMonth}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<PerformedService> services(
            @RequestDomain Domain domain,
            @PathVariable("forMonth") YearMonth forMonth) {
        return performedServicesService.services(domain, forMonth).stream()
                .map(c -> mapper.map(c, PerformedService.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public PerformedService createService(
            @RequestBodyWithDomain(
                    transportClass = PerformedService.class,
                    mapperName = CREATE_PERFORMED_SERVICE,
                    create = true
            )
            @Valid pl.sg.accountant.model.ledger.PerformedService performedService) {
        return mapper.map(performedServicesService.create(performedService), PerformedService.class);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public PerformedService updateService(
            @RequestBodyWithDomain(
                    transportClass = PerformedService.class,
                    mapperName = UPDATE_PERFORMED_SERVICE
            )
            @Valid pl.sg.accountant.model.ledger.PerformedService service) {
        return mapper.map(performedServicesService.update(service), PerformedService.class);
    }
}
