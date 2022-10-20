package pl.sg.ipr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.ipr.model.IntellectualProperty;

import java.util.List;

public interface IntellectualPropertyRepository extends JpaRepository<IntellectualProperty, Integer> {

    List<IntellectualProperty> findAllByDomain_Id(int domainId);

}
