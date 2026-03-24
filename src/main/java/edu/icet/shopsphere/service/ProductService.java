package edu.icet.shopsphere.service;

import edu.icet.shopsphere.dto.product.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Long id);
}
