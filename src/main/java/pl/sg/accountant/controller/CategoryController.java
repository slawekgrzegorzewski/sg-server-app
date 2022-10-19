package pl.sg.accountant.controller;

import pl.sg.accountant.transport.billings.Category;
import pl.sg.application.model.Domain;

import java.util.List;

public interface CategoryController {

    List<Category> getCategories(Domain domain);

    Category addCategory(pl.sg.accountant.model.billings.Category category);

    Category updateCategory(pl.sg.accountant.model.billings.Category category);
}
