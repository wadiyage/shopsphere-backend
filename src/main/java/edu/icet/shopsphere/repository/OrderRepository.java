package edu.icet.shopsphere.repository;

import edu.icet.shopsphere.entity.Order;
import edu.icet.shopsphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);
}
