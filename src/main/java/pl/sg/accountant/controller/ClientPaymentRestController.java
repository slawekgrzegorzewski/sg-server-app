package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.accounts.ClientPayment;
import pl.sg.accountant.service.ClientPaymentsService;
import pl.sg.accountant.transport.accounts.ClientPaymentTO;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import javax.validation.Valid;
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
    public List<ClientPaymentTO> clientPayments(
            @RequestDomain Domain domain,
            @PathVariable YearMonth forMonth) {
        return clientPaymentsService.clientPayments(domain).stream()
                .map(c -> mapper.map(c, ClientPaymentTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ClientPaymentTO createClientPayment(
            @RequestBodyWithDomain(
                    transportClass = ClientPaymentTO.class,
                    mapperName = CREATE_CLIENT_PAYMENT,
                    create = true
            )
            @Valid ClientPayment clientPayment) {
        return mapper.map(clientPaymentsService.create(clientPayment), ClientPaymentTO.class);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ClientPaymentTO updateClientPayment(
            @RequestBodyWithDomain(
                    transportClass = ClientPaymentTO.class,
                    mapperName = UPDATE_CLIENT_PAYMENT
            )
            @Valid ClientPayment clientPayment) {
        return mapper.map(clientPaymentsService.update(clientPayment), ClientPaymentTO.class);
    }
}
