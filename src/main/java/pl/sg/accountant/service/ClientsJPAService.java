package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.accounts.Client;
import pl.sg.accountant.repository.ClientRepository;
import pl.sg.application.model.Domain;

import java.util.List;

@Component
public class ClientsJPAService implements ClientsService {

    private final ClientRepository clientRepository;

    public ClientsJPAService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> clients(Domain domain) {
        return clientRepository.findByDomain(domain);
    }

    @Override
    public Client create(Client client) {
        client.setId(null);
        return this.clientRepository.save(client);
    }

    @Override
    public Client update(Client client) {
        return this.clientRepository.save(client);
    }
}
