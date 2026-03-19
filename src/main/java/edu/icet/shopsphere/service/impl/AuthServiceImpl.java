package edu.icet.shopsphere.service.impl;

import edu.icet.shopsphere.dto.LoginRequest;
import edu.icet.shopsphere.dto.RegisterRequest;
import edu.icet.shopsphere.dto.UserResponse;
import edu.icet.shopsphere.entity.Role;
import edu.icet.shopsphere.entity.User;
import edu.icet.shopsphere.exception.EmailAlreadyExistsException;
import edu.icet.shopsphere.repository.UserRepository;
import edu.icet.shopsphere.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        } else {
            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword())) // Hash the password before saving
                    .role(Role.CUSTOMER)
                    .build();

            User savedUser = userRepository.save(user);
            return UserResponse.builder()
                    .id(savedUser.getId())
                    .firstName(savedUser.getFirstName())
                    .lastName(savedUser.getLastName())
                    .email(savedUser.getEmail())
                    .role(savedUser.getRole())
                    .build();
        }
    }

    @Override
    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password is incorrect");
        }
        return "Login successful for user: " + user.getEmail();
    }
}
