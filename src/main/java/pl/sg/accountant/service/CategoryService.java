package pl.sg.accountant.service;

import pl.sg.accountant.model.billings.Category;
import pl.sg.application.model.ApplicationUser;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> findById(Integer id);

    Optional<Category> findByIdAndApplicationUser(Integer id, ApplicationUser applicationUser);

    List<Category> getAllForUser(ApplicationUser user);

    List<Category> getAll();

    Category create(Category category, ApplicationUser user);

    Category update(Integer id, Category category, ApplicationUser user);

    void delete(Category category);
}
