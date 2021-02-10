package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.billings.Category;
import pl.sg.accountant.repository.CategoryRepository;
import pl.sg.application.model.ApplicationUser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CategoryJPAService implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryJPAService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Category> findByIdAndApplicationUser(ApplicationUser user, Integer id) {
        return categoryRepository.getByIdEqualsAndApplicationUserEquals(id, user)
                .map(c -> {
                    user.validateDomain(c.getDomain());
                    return c;
                });
    }

    @Override
    public List<Category> getForUser(ApplicationUser user, int domainId) {
        return categoryRepository.findByApplicationUserEqualsAndDomainIdEquals(user, domainId);
    }

    @Override
    public Category create(ApplicationUser user, Category category) {
        user.validateAdminDomain(category.getDomain());
        category.setId(null);
        return categoryRepository.save(category);
    }

    @Override
    public Category update(ApplicationUser user, Category category) {
        final Category dbValue = categoryRepository.getOne(category.getId());
        user.validateAdminDomain(dbValue.getDomain());
        return categoryRepository.save(category);
    }
}
