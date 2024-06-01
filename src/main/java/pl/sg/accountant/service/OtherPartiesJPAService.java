package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.model.bussines.Client;
import pl.sg.accountant.repository.ClientRepository;
import pl.sg.application.model.Domain;
import pl.sg.loans.utils.LoansException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class OtherPartiesJPAService implements OtherPartiesService {

    private final ClientRepository clientRepository;

    public OtherPartiesJPAService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> clients(Domain domain) {
        return clientRepository.findByDomain(domain);
    }

    @Override
    public Client createClient(String name, Domain domain) {
        Client client = new Client();
        client.setName(name);
        client.setDomain(domain);
        return this.clientRepository.save(client);
    }

    @Override
    public Client updateClient(UUID clientPublicId, Domain domain, String name) {
        Client client = Objects.requireNonNull(clientRepository.findByPublicId(clientPublicId));
        validateDomain(domain, client.getDomain());
        client.setName(name);
        return this.clientRepository.save(client);
    }

    @Override
    public void deleteClient(UUID clientPublicId, Domain domain) {
        Client client = Objects.requireNonNull(clientRepository.findByPublicId(clientPublicId));
        validateDomain(domain, client.getDomain());
        this.clientRepository.delete(client);
    }


    private void validateDomain(Domain domain, Domain otherDomain) {
        if (!domain.getId().equals(otherDomain.getId())) {
            throw new AccountsException("Domains doesn't match " + domain.getId() + ":" + otherDomain.getId());
        }
    }
}
