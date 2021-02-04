package pl.sg.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.application.model.Domain;

public interface DomainRepository extends JpaRepository<Domain, Integer> {
}
