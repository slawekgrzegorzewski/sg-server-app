package pl.sg.ip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.ip.model.IntellectualProperty;

import java.util.List;

public interface IntellectualPropertyRepository extends JpaRepository<IntellectualProperty, Integer> {

    List<IntellectualProperty> findAllByDomain_Id(int domainId);

}
