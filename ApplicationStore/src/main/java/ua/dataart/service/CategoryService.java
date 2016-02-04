package ua.dataart.service;

import ua.dataart.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.dataart.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllApplicationCategories(){
        return categoryRepository.findAll();
    }
    public Category getCategoryByName(String name){
        return categoryRepository.getCategoryByName(name);
    }
}
