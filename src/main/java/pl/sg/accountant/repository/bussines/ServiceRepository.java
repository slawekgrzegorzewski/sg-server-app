package pl.sg.accountant.repository.bussines;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.bussines.Service;
import pl.sg.application.model.Domain;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Integer> {

    List<Service> findByDomain(Domain domain);

}
