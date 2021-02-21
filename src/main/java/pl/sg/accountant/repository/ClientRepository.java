package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.accounts.Client;
import pl.sg.application.model.Domain;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    List<Client> findByDomain(Domain domain);

}
