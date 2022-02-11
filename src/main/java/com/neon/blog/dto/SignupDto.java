package com.neon.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SignupDto {
    @NotEmpty(message = "email must not be empty")
    private String email;
    @NotEmpty(message = "name must not be empty")
    private String name;
    @NotEmpty(message = "password must not be empty")
    private String password;
}
