package pl.sg.accountant.controller;

import pl.sg.accountant.model.billings.Category;
import pl.sg.accountant.transport.billings.CategoryTO;
import pl.sg.application.model.Domain;

import java.util.List;

public interface CategoryController {

    List<CategoryTO> getCategories(Domain domain);

    CategoryTO addCategory(Category category);

    CategoryTO updateCategory(Category category);
}
