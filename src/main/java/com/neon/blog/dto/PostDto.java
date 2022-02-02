package com.neon.blog.dto;

import lombok.Data;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String description;
}
