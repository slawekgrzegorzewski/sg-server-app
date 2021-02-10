package pl.sg.accountant.controller;

import org.springframework.http.ResponseEntity;
import pl.sg.accountant.transport.billings.CategoryTO;
import pl.sg.application.model.ApplicationUser;

import java.util.List;

public interface CategoryController {

    ResponseEntity<List<CategoryTO>> getCategories(ApplicationUser user, int domainId);

    ResponseEntity<CategoryTO> addCategory(ApplicationUser user, CategoryTO categoryTO);
}
