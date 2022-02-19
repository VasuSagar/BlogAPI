package com.neon.blog.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;

@Component
@AllArgsConstructor
public class WebSocketAuthenticationService {
    private JwtTokenProvider jwtTokenProvider;
    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(final String password) throws AuthenticationException {
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Password was null or empty.");
        }
        // Add your own logic for retrieving user in fetchUserFromDb()
        if (fetchUserFromDb(password) == null) {
            throw new BadCredentialsException("Bad credentials for user "+jwtTokenProvider.getEmailFromToken(password));
        }

        // null credentials, we do not pass the password along
        return new UsernamePasswordAuthenticationToken(
                jwtTokenProvider.getEmailFromToken(password),
                null,
                Collections.singleton((GrantedAuthority) () -> "ADMIN") // MUST provide at least one role
        );
    }

    private String fetchUserFromDb(String password) {
        System.out.println("origin:"+password);
        if(StringUtils.hasText(password) && jwtTokenProvider.validateToken(password))
        {
            return password;
        }
        return null;
    }
}
