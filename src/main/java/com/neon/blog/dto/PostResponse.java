package com.neon.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private List<PostDto> posts;
    private int currentPage;
    private Long totalElements;
    private int totalPages;
    private int pageSize;
    private boolean isLast;
}
