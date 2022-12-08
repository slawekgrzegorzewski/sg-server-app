package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.service.ClientsService;
import pl.sg.accountant.transport.accounts.Client;
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
    public List<Client> clients(@RequestDomain Domain domain) {
        return clientsService.clients(domain).stream()
                .map(c -> mapper.map(c, Client.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Client createClient(
            @RequestBodyWithDomain(
                    transportClass = Client.class,
                    mapperName = CREATE_CLIENT,
                    create = true
            )
            @Valid pl.sg.accountant.model.accounts.Client client) {
        return mapper.map(clientsService.create(client), Client.class);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Client updateClient(
            @RequestBodyWithDomain(
                    transportClass = Client.class,
                    mapperName = UPDATE_CLIENT
            )
            @Valid pl.sg.accountant.model.accounts.Client client) {
        return mapper.map(clientsService.update(client), Client.class);
    }
}
