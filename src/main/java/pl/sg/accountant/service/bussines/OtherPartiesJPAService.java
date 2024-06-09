package pl.sg.accountant.service.bussines;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.model.bussines.Client;
import pl.sg.accountant.model.bussines.Supplier;
import pl.sg.accountant.repository.bussines.ClientRepository;
import pl.sg.accountant.repository.bussines.SupplierRepository;
import pl.sg.application.model.Domain;
import pl.sg.application.repository.DomainRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static pl.sg.application.DomainValidator.validateDomain;

@Component
public class OtherPartiesJPAService implements OtherPartiesService {

    private final ClientRepository clientRepository;
    private final DomainRepository domainRepository;
    private final SupplierRepository supplierRepository;

    public OtherPartiesJPAService(ClientRepository clientRepository, DomainRepository domainRepository, SupplierRepository supplierRepository) {
        this.clientRepository = clientRepository;
        this.domainRepository = domainRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public List<Client> clients(int domainId) {
        return clientRepository.findByDomain(domainRepository.getReferenceById(domainId));
    }

    @Override
    public Client createClient(String name, int domainId) {
        Client client = new Client();
        client.setName(name);
        client.setDomain(domainRepository.getReferenceById(domainId));
        return this.clientRepository.save(client);
    }

    @Override
    public Client updateClient(UUID clientPublicId, int domainId, String name) {
        Client client = Objects.requireNonNull(clientRepository.findByPublicId(clientPublicId));
        validateDomain(domainRepository.getReferenceById(domainId), client.getDomain());
        client.setName(name);
        return this.clientRepository.save(client);
    }

    @Override
    public void deleteClient(UUID clientPublicId, int domainId) {
        Client client = Objects.requireNonNull(clientRepository.findByPublicId(clientPublicId));
        validateDomain(domainRepository.getReferenceById(domainId), client.getDomain());
        this.clientRepository.delete(client);
    }

    @Override
    public List<Supplier> suppliers(int domainId) {
        return supplierRepository.findByDomain(domainRepository.getReferenceById(domainId));
    }

    @Override
    public Supplier createSupplier(String name, int domainId) {
        Supplier supplier = new Supplier();
        supplier.setName(name);
        supplier.setDomain(domainRepository.getReferenceById(domainId));
        return this.supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(UUID supplierPublicId, int domainId, String name) {
        Supplier client = Objects.requireNonNull(supplierRepository.findByPublicId(supplierPublicId));
        validateDomain(domainRepository.getReferenceById(domainId), client.getDomain());
        client.setName(name);
        return this.supplierRepository.save(client);
    }

    @Override
    public void deleteSupplier(UUID clientPublicId, int domainId) {
        Supplier client = Objects.requireNonNull(supplierRepository.findByPublicId(clientPublicId));
        validateDomain(domainRepository.getReferenceById(domainId), client.getDomain());
        this.supplierRepository.delete(client);
    }
}
