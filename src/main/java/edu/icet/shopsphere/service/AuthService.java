package edu.icet.shopsphere.service;

import edu.icet.shopsphere.dto.LoginRequest;
import edu.icet.shopsphere.dto.RegisterRequest;
import edu.icet.shopsphere.dto.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    String login(LoginRequest request);
}
