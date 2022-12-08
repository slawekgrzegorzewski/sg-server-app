package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.service.ServicesService;
import pl.sg.accountant.transport.accounts.Service;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static pl.sg.Application.CREATE_CLIENT;
import static pl.sg.Application.UPDATE_CLIENT;

@RestController
@RequestMapping("/services")
@Validated
public class ServicesRestController implements ServicesController {
    private final ServicesService servicesService;
    private final ModelMapper mapper;

    public ServicesRestController(ServicesService servicesService, ModelMapper mapper) {
        this.servicesService = servicesService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<Service> services(@RequestDomain Domain domain) {
        return servicesService.services(domain).stream()
                .map(c -> mapper.map(c, Service.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Service createService(
            @RequestBodyWithDomain(
                    transportClass = Service.class,
                    mapperName = CREATE_CLIENT,
                    create = true
            )
            @Valid pl.sg.accountant.model.accounts.Service service) {
        return mapper.map(servicesService.create(service), Service.class);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Service updateService(
            @RequestBodyWithDomain(
                    transportClass = Service.class,
                    mapperName = UPDATE_CLIENT
            )
            @Valid pl.sg.accountant.model.accounts.Service service) {
        return mapper.map(servicesService.update(service), Service.class);
    }
}
