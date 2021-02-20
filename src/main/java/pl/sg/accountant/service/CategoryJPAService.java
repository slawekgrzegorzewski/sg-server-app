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
    public List<Category> getForDomain(Domain domain) {
        return categoryRepository.findByDomain(domain);
    }

    @Override
    public Category create(Category category) {
        category.setId(null);
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        return categoryRepository.save(category);
    }
}
