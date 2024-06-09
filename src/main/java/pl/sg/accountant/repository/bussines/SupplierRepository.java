package pl.sg.accountant.repository.bussines;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.bussines.Supplier;
import pl.sg.application.model.Domain;

import java.util.List;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    List<Supplier> findByDomain(Domain domain);

    Supplier findByPublicId(UUID publicId);

}
