package pl.sg.accountant.controller;

import pl.sg.accountant.transport.billings.CategoryTO;
import pl.sg.application.model.ApplicationUser;

import java.util.List;

public interface CategoryController {

    List<CategoryTO> getCategories(ApplicationUser user, int domainId);

    CategoryTO addCategory(ApplicationUser user, CategoryTO categoryTO);
}
