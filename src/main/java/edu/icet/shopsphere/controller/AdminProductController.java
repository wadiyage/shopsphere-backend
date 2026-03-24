package edu.icet.shopsphere.controller;

import edu.icet.shopsphere.dto.product.ProductRequest;
import edu.icet.shopsphere.dto.product.ProductResponse;
import edu.icet.shopsphere.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;
    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest request) {
        return adminProductService.createProduct(request);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        return adminProductService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        adminProductService.deleteProduct(id);
    }
}
