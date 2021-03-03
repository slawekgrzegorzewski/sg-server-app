package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.accounts.PerformedService;
import pl.sg.application.model.Domain;

import java.util.List;

public interface PerformedServiceRepository extends JpaRepository<PerformedService, Integer> {

    List<PerformedService> findByDomain(Domain domain);

}
