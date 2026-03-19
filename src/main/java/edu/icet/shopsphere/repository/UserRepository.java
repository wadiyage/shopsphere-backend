package edu.icet.shopsphere.repository;

import edu.icet.shopsphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email); // Optional to handle the case where a user with the given email might not exist
}
