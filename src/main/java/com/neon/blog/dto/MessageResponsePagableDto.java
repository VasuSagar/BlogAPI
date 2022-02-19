package com.neon.blog.dto;

import lombok.Data;

import java.util.List;

@Data
public class MessageResponsePagableDto {
    private List<MessageResponseDto> messages;
    private int currentPage;
    private Long totalElements;
    private int totalPages;
    private int pageSize;
    private boolean isLast;
}
