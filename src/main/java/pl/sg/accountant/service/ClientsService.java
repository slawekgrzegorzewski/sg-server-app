package pl.sg.accountant.service;

import pl.sg.accountant.model.accounts.Client;
import pl.sg.application.model.Domain;

import java.util.List;

public interface ClientsService {
    List<Client> clients(Domain domain);

    Client create(Client client);

    Client update(Client client);
}
