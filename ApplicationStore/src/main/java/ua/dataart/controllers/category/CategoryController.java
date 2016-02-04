package ua.dataart.controllers.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.dataart.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.dataart.service.CategoryService;

import java.util.List;

@RestController
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/app_categories", method = RequestMethod.GET)
    public List<Category> getCustomerTypes() {
        logger.debug("Retrieving application categories. ");
        return categoryService.getAllApplicationCategories();
    }
}
