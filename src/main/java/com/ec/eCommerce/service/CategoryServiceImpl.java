package com.ec.eCommerce.service;

import com.ec.eCommerce.models.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final List<Category> categories;

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public void addCategory(String categoryName) {
        Category category = new Category(categories.size()+1, categoryName);
        categories.add(category);
    }

    @Override
    public Optional<Category> getCategoryById(int categoryId) {
        int index = categoryId-1;
        if(index >= 0 && index< categories.size()){
            return Optional.of(categories.get(index));
        }else{
            return Optional.empty();
        }
    }
}
