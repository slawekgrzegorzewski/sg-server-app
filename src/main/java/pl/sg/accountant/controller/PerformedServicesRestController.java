package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.accounts.PerformedService;
import pl.sg.accountant.service.PerformedServicesService;
import pl.sg.accountant.transport.accounts.PerformedServiceTO;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import javax.validation.Valid;
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
    public List<PerformedServiceTO> services(
            @RequestDomain Domain domain,
            @PathVariable YearMonth forMonth) {
        return performedServicesService.services(domain).stream()
                .map(c -> mapper.map(c, PerformedServiceTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public PerformedServiceTO createService(
            @RequestBodyWithDomain(
                    transportClass = PerformedServiceTO.class,
                    mapperName = CREATE_PERFORMED_SERVICE,
                    create = true
            )
            @Valid PerformedService performedService) {
        return mapper.map(performedServicesService.create(performedService), PerformedServiceTO.class);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public PerformedServiceTO updateService(
            @RequestBodyWithDomain(
                    transportClass = PerformedServiceTO.class,
                    mapperName = UPDATE_PERFORMED_SERVICE
            )
            @Valid PerformedService service) {
        return mapper.map(performedServicesService.update(service), PerformedServiceTO.class);
    }
}
