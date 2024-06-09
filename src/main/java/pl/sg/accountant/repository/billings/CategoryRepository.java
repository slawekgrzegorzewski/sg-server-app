package pl.sg.accountant.repository.billings;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.billings.Category;
import pl.sg.application.model.Domain;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByDomain(Domain domain);

}
