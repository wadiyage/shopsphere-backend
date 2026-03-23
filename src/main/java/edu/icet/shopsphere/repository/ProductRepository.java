package edu.icet.shopsphere.repository;

import edu.icet.shopsphere.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
