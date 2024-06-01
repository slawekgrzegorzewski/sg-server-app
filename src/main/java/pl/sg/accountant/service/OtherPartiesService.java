package pl.sg.accountant.service;

import pl.sg.accountant.model.bussines.Client;
import pl.sg.application.model.Domain;

import java.util.List;
import java.util.UUID;

public interface OtherPartiesService {
    List<Client> clients(Domain domain);

    Client createClient(String name, Domain domain);

    Client updateClient(UUID clientId, Domain domain, String name);

    void deleteClient(UUID clientPublicId, Domain referenceById);
}
