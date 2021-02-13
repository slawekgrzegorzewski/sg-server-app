package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.billings.Category;
import pl.sg.accountant.repository.CategoryRepository;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;
import pl.sg.application.service.DomainService;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryJPAService implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final DomainService domainService;

    public CategoryJPAService(CategoryRepository categoryRepository, DomainService domainService) {
        this.categoryRepository = categoryRepository;
        this.domainService = domainService;
    }

    @Override
    public Optional<Category> findByIdAndApplicationUser(ApplicationUser user, Integer id) {
        return categoryRepository.findById(id)
                .map(c -> {
                    user.validateDomain(c.getDomain());
                    return c;
                });
    }

    @Override
    public List<Category> getForUser(ApplicationUser user, int domainId) {
        final Domain domain = domainService.getById(domainId);
        user.validateDomain(domain);
        return categoryRepository.findByDomain(domain);
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
