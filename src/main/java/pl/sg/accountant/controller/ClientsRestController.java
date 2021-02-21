package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.accounts.Client;
import pl.sg.accountant.service.ClientsService;
import pl.sg.accountant.transport.accounts.ClientTO;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.PathVariableWithDomain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.application.service.DomainService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static pl.sg.Application.CREATE_CLIENT;
import static pl.sg.Application.UPDATE_CLIENT;

@RestController
@RequestMapping("/clients")
@Validated
public class ClientsRestController implements ClientsController {
    private final ClientsService clientsService;
    private final ModelMapper mapper;

    public ClientsRestController(ClientsService clientsService, ModelMapper mapper) {
        this.clientsService = clientsService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<ClientTO> clients(@RequestDomain Domain domain) {
        return clientsService.clients(domain).stream()
                .map(c -> mapper.map(c, ClientTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ClientTO createClient(
            @RequestBodyWithDomain(
                    transportClass = ClientTO.class,
                    mapperName = CREATE_CLIENT,
                    create = true
            )
            @Valid Client client) {
        return mapper.map(clientsService.create(client), ClientTO.class);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ClientTO updateClient(
            @RequestBodyWithDomain(
                    transportClass = ClientTO.class,
                    mapperName = UPDATE_CLIENT
            )
            @Valid Client client) {
        return mapper.map(clientsService.update(client), ClientTO.class);
    }
}
