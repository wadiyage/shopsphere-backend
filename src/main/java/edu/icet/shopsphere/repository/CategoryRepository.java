package edu.icet.shopsphere.repository;

import edu.icet.shopsphere.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
