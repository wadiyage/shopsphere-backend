package edu.icet.shopsphere.controller;

import edu.icet.shopsphere.dto.RegisterRequest;
import edu.icet.shopsphere.dto.UserResponse;
import edu.icet.shopsphere.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse register(RegisterRequest request) {
        return authService.register(request);
    }
}
