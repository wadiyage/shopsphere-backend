package edu.icet.shopsphere.controller;

import edu.icet.shopsphere.dto.auth.LoginRequest;
import edu.icet.shopsphere.dto.auth.LoginResponse;
import edu.icet.shopsphere.dto.auth.RegisterRequest;
import edu.icet.shopsphere.dto.user.UserResponse;
import edu.icet.shopsphere.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
