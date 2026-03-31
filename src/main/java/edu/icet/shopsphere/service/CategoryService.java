package edu.icet.shopsphere.service;

import edu.icet.shopsphere.dto.category.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
}
