package pl.sg.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.application.model.ApplicationUserDomainRelation;

public interface ApplicationUserDomainRelationRepository extends JpaRepository<ApplicationUserDomainRelation, Integer> {
}