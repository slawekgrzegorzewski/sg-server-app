package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.billings.Category;
import pl.sg.accountant.service.CategoryService;
import pl.sg.accountant.transport.billings.CategoryTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.security.annotations.RequestUser;
import pl.sg.application.security.annotations.TokenBearerAuth;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryRestController implements CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper mapper;

    public CategoryRestController(CategoryService categoryService,
                                  ModelMapper mapper) {
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping("/{domainId}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<CategoryTO> getCategories(@RequestUser ApplicationUser user,
                                          @PathVariable("domainId") int domainId) {
        return categoryService.getForUser(user, domainId).stream()
                .map(category -> mapper.map(category, CategoryTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping()
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public CategoryTO addCategory(@RequestUser ApplicationUser user,
                                  @RequestBody CategoryTO categoryTO) {
        Category created;
        if (categoryTO.getId() == null) {
            created = categoryService.create(user, mapper.map(categoryTO, Category.class));
        } else {
            created = categoryService.findByIdAndApplicationUser(user, categoryTO.getId())
                    .map(c -> applyChanges(categoryTO, c))
                    .map(c -> categoryService.update(user, c))
                    .orElseThrow(() -> new EntityNotFoundException("Category to update does not exist."));
        }
        return mapper.map(created, CategoryTO.class);
    }

    private Category applyChanges(CategoryTO source, Category destination) {
        destination.setName(source.getName());
        destination.setDescription(source.getDescription());
        return destination;
    }
}
