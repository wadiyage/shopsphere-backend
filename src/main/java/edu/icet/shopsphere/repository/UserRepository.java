package edu.icet.shopsphere.repository;

import edu.icet.shopsphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
