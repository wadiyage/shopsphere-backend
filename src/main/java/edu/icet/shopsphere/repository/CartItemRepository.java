package edu.icet.shopsphere.repository;

import edu.icet.shopsphere.entity.CartItem;
import edu.icet.shopsphere.entity.Product;
import edu.icet.shopsphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByUser(User user);
    CartItem findByUserAndProduct(User user, Product product);
}
