package pl.sg.accountant.controller;

import pl.sg.accountant.transport.accounts.Client;
import pl.sg.application.model.Domain;

import jakarta.validation.Valid;
import java.util.List;

public interface ClientsController {

    List<Client> clients(Domain domain);

    Client createClient(@Valid pl.sg.accountant.model.accounts.Client client);

    Client updateClient(@Valid pl.sg.accountant.model.accounts.Client client);
}
