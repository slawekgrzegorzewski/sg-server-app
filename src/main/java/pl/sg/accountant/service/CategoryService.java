package pl.sg.accountant.service;

import pl.sg.accountant.model.billings.Category;
import pl.sg.application.model.ApplicationUser;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Optional<Category> findByIdAndApplicationUser(ApplicationUser applicationUser, Integer id);

    List<Category> getForUser(ApplicationUser user, int domainId);

    Category create(ApplicationUser user, Category category);

    Category update(ApplicationUser user, Category category);

}
