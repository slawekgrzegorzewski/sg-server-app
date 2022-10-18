package pl.sg.ipr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.AccountantSettings;
import pl.sg.application.model.Domain;
import pl.sg.ipr.model.IntellectualProperty;

import java.util.List;
import java.util.Optional;

public interface IntellectualPropertyRepository extends JpaRepository<IntellectualProperty, Integer> {

    List<IntellectualProperty> findAllByDomain(Domain domain);

}
