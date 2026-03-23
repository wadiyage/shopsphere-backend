package edu.icet.shopsphere.service;

import edu.icet.shopsphere.dto.product.ProductRequest;
import edu.icet.shopsphere.dto.product.ProductResponse;

public interface AdminProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
}
