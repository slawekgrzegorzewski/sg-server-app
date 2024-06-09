package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.service.billings.CategoryService;
import pl.sg.accountant.transport.billings.Category;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static pl.sg.Application.CREATE_CATEGORY;
import static pl.sg.Application.UPDATE_CATEGORY;

@RestController
@RequestMapping("/categories")
public class CategoryRestController implements CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper mapper;

    public CategoryRestController(CategoryService categoryService, ModelMapper mapper) {
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<Category> getCategories(@RequestDomain Domain domain) {
        return categoryService.getForDomain(domain).stream()
                .map(category -> mapper.map(category, Category.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Category addCategory(
            @RequestBodyWithDomain(
                    create = true,
                    transportClass = Category.class,
                    domainAdmin = true,
                    mapperName = CREATE_CATEGORY)
            @Valid pl.sg.accountant.model.billings.Category category) {
        return mapper.map(categoryService.create(category), Category.class);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Category updateCategory(
            @RequestBodyWithDomain(
                    transportClass = Category.class,
                    domainAdmin = true,
                    mapperName = UPDATE_CATEGORY)
            @Valid pl.sg.accountant.model.billings.Category category) {
        return mapper.map(categoryService.update(category), Category.class);
    }
}
