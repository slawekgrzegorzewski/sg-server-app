package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.accounts.PerformedServicePayment;
import pl.sg.accountant.service.PerformedServicePaymentsService;
import pl.sg.accountant.transport.accounts.PerformedServicePaymentTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.MapRequestBody;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.RequestUser;
import pl.sg.application.security.annotations.TokenBearerAuth;

import jakarta.validation.Valid;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import static pl.sg.Application.CREATE_PERFORMED_SERVICE_PAYMENT;
import static pl.sg.Application.UPDATE_PERFORMED_SERVICE_PAYMENT;

@RestController
@RequestMapping("/performed-service-payments")
@Validated
public class PerformedServicePaymentsRestController implements PerformedServicePaymentsController {

    private final PerformedServicePaymentsService performedServicePaymentsService;
    private final ModelMapper mapper;

    public PerformedServicePaymentsRestController(PerformedServicePaymentsService performedServicePaymentsService, ModelMapper mapper) {
        this.performedServicePaymentsService = performedServicePaymentsService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping("/{forMonth}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<PerformedServicePaymentTO> payments(
            @RequestDomain Domain domain,
            @PathVariable YearMonth forMonth) {
        return performedServicePaymentsService.payments(domain).stream()
                .map(c -> mapper.map(c, PerformedServicePaymentTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public PerformedServicePaymentTO createPayment(
            @RequestUser ApplicationUser user,
            @MapRequestBody(
                    transportClass = PerformedServicePaymentTO.class,
                    mapperName = CREATE_PERFORMED_SERVICE_PAYMENT
            )
            @Valid PerformedServicePayment payment) {
        return mapper.map(performedServicePaymentsService.create(user, payment), PerformedServicePaymentTO.class);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public PerformedServicePaymentTO updatePayment(
            @RequestUser ApplicationUser user,
            @MapRequestBody(
                    transportClass = PerformedServicePaymentTO.class,
                    mapperName = UPDATE_PERFORMED_SERVICE_PAYMENT
            )
            @Valid PerformedServicePayment payment) {
        return mapper.map(performedServicePaymentsService.update(payment), PerformedServicePaymentTO.class);
    }
}
