package com.neon.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    //title should not be null or empty and have atleast 2 chars
    @NotEmpty(message = "title must not be empty")
    @Size(min=2,message = "title should have at least 2 characters")
    private String title;
    @NotEmpty(message = "content must not be empty")
    private String content;
    @NotEmpty(message = "description must not be empty")
    @Size(min=5,message = "description should have at least 5 characters")
    private String description;

    private Set<CommentDto> comments;
    private Instant createdDate;
    private String userName;
    private Long userId;
}
