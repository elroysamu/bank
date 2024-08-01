package com.ec.eCommerce.service;

import com.ec.eCommerce.models.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> getCategories();

    void addCategory(String categoryName);

    Optional<Category> getCategoryById(int categoryId);
}
