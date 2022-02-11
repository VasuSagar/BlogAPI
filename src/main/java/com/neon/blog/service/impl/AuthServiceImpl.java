package com.neon.blog.service.impl;

import com.neon.blog.dto.AuthResponse;
import com.neon.blog.dto.LoginDto;
import com.neon.blog.dto.SignupDto;
import com.neon.blog.exception.BlogAPIException;
import com.neon.blog.model.Role;
import com.neon.blog.model.User;
import com.neon.blog.repository.RoleRepository;
import com.neon.blog.repository.UserRepository;
import com.neon.blog.security.JwtTokenProvider;
import com.neon.blog.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public AuthResponse login(LoginDto loginDto) {
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token=jwtTokenProvider.generateToken(authentication);
        return new AuthResponse(token,loginDto.getEmail());

    }

    @Override
    public void signup(SignupDto signupDto) {
        if(userRepository.existsByEmail(signupDto.getEmail())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"User already exists in database with email:"+signupDto.getEmail());
        }

        User user=new User();
        user.setEmail(signupDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setName(signupDto.getName());

        Role roles=roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);


    }

    @Override
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Email name not found - " + principal.getUsername()));
    }
}
