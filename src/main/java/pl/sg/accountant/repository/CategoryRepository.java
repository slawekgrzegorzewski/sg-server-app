package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.billings.Category;
import pl.sg.application.model.ApplicationUser;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> getByIdEqualsAndApplicationUserEquals(Integer categoryId, ApplicationUser user);

    List<Category> findByApplicationUserEqualsAndDomainIdEquals(ApplicationUser user, int domainId);
}
