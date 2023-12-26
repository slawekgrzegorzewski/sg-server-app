package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.service.ClientPaymentsService;
import pl.sg.accountant.transport.accounts.ClientPayment;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import jakarta.validation.Valid;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import static pl.sg.Application.CREATE_CLIENT_PAYMENT;
import static pl.sg.Application.UPDATE_CLIENT_PAYMENT;

@RestController
@RequestMapping("/client-payments")
@Validated
public class ClientPaymentRestController implements ClientPaymentController {
    private final ClientPaymentsService clientPaymentsService;
    private final ModelMapper mapper;

    public ClientPaymentRestController(ClientPaymentsService clientPaymentsService, ModelMapper mapper) {
        this.clientPaymentsService = clientPaymentsService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping("/{forMonth}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<ClientPayment> clientPayments(
            @RequestDomain Domain domain,
            @PathVariable("forMonth") YearMonth forMonth) {
        return clientPaymentsService.clientPayments(domain, forMonth).stream()
                .map(c -> mapper.map(c, ClientPayment.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ClientPayment createClientPayment(
            @RequestBodyWithDomain(
                    transportClass = ClientPayment.class,
                    mapperName = CREATE_CLIENT_PAYMENT,
                    create = true
            )
            @Valid pl.sg.accountant.model.accounts.ClientPayment clientPayment) {
        return mapper.map(clientPaymentsService.create(clientPayment), ClientPayment.class);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ClientPayment updateClientPayment(
            @RequestBodyWithDomain(
                    transportClass = ClientPayment.class,
                    mapperName = UPDATE_CLIENT_PAYMENT
            )
            @Valid pl.sg.accountant.model.accounts.ClientPayment clientPayment) {
        return mapper.map(clientPaymentsService.update(clientPayment), ClientPayment.class);
    }
}
