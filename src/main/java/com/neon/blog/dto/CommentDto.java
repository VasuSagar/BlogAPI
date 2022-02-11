package com.neon.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
public class CommentDto {
    private Long id;
    @NotEmpty(message = "body should not be empty")
    @Size(min =5,message = "body should have atleast 5 characters")
    private String body;

    private Instant createdDate;
    private String userName;
    private Long userId;
}
