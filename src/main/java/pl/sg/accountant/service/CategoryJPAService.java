package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.billings.Category;
import pl.sg.accountant.repository.CategoryRepository;
import pl.sg.application.model.ApplicationUser;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryJPAService implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryJPAService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> findByIdAndApplicationUser(Integer id, ApplicationUser applicationUser) {
        return categoryRepository.getByIdEqualsAndApplicationUserEquals(id, applicationUser);
    }

    @Override
    public List<Category> getAllForUser(ApplicationUser user) {
        return categoryRepository.findByApplicationUserEquals(user);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category create(Category category, ApplicationUser user) {
        category.setApplicationUser(user);
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Integer id, Category category, ApplicationUser user) {
        category.setApplicationUser(user);
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Category category) {
        categoryRepository.delete(category);
    }
}
