package edu.icet.shopsphere.service.impl;

import edu.icet.shopsphere.dto.product.ProductRequest;
import edu.icet.shopsphere.dto.product.ProductResponse;
import edu.icet.shopsphere.entity.Category;
import edu.icet.shopsphere.entity.Product;
import edu.icet.shopsphere.repository.CategoryRepository;
import edu.icet.shopsphere.repository.ProductRepository;
import edu.icet.shopsphere.service.AdminProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .imageUrl(request.getImageUrl())
                .category(category)
                .build();

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .categoryName(product.getCategory().getName())
                .build();
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product exiting = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

        exiting.setName(request.getName());
        exiting.setDescription(request.getDescription());
        exiting.setPrice(request.getPrice());
        exiting.setStockQuantity(request.getStockQuantity());
        exiting.setImageUrl(request.getImageUrl());
        exiting.setCategory(category);

        Product updated = productRepository.save(exiting);
        return mapToResponse(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        Product exiting = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(exiting);
    }
}
