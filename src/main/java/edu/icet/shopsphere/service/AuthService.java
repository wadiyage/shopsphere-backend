package edu.icet.shopsphere.service;

import edu.icet.shopsphere.dto.auth.LoginRequest;
import edu.icet.shopsphere.dto.auth.LoginResponse;
import edu.icet.shopsphere.dto.auth.RegisterRequest;
import edu.icet.shopsphere.dto.user.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
