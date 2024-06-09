package pl.sg.accountant.service.bussines;

import pl.sg.accountant.model.bussines.Client;
import pl.sg.accountant.model.bussines.Supplier;

import java.util.List;
import java.util.UUID;

public interface OtherPartiesService {
    List<Client> clients(int domainId);

    Client createClient(String name, int domainId);

    Client updateClient(UUID clientId, int domainId, String name);

    void deleteClient(UUID clientPublicId, int domainId);

    List<Supplier> suppliers(int domainId);

    Supplier createSupplier(String name, int domainId);

    Supplier updateSupplier(UUID clientId, int domainId, String name);

    void deleteSupplier(UUID clientPublicId, int domainId);
}
