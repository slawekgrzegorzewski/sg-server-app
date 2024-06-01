package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.bussines.Client;
import pl.sg.application.model.Domain;

import java.util.List;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    List<Client> findByDomain(Domain domain);

    Client findByPublicId(UUID publicId);

}
