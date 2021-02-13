package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.billings.Category;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByDomain(Domain domain);

}
