package com.neon.blog.service;

import com.neon.blog.dto.AuthResponse;
import com.neon.blog.dto.LoginDto;
import com.neon.blog.dto.SignupDto;
import com.neon.blog.model.User;

public interface AuthService {
    AuthResponse login(LoginDto loginDto);
    void signup(SignupDto signupDto);

    User getCurrentUser();
}
