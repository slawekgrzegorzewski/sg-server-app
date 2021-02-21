package pl.sg.accountant.controller;

import pl.sg.accountant.model.accounts.Client;
import pl.sg.accountant.transport.accounts.ClientTO;
import pl.sg.application.model.Domain;

import javax.validation.Valid;
import java.util.List;

public interface ClientsController {

    List<ClientTO> clients(Domain domain);

    ClientTO createClient(@Valid Client client);

    ClientTO updateClient(@Valid Client client);
}
