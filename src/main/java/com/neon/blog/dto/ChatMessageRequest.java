package com.neon.blog.dto;

import lombok.Data;

@Data
public class ChatMessageRequest {
    private String message;
    private Long senderId;
}
