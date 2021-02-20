package pl.sg.accountant.service;

import pl.sg.accountant.model.billings.Category;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Optional<Category> findByIdAndApplicationUser(ApplicationUser applicationUser, Integer id);

    List<Category> getForDomain(Domain domain);

    Category create(Category category);

    Category update(Category category);

}
