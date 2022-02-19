package com.neon.blog.dto;

import lombok.Data;

@Data
public class MessageResponseDto {
    private Long messageId;
    private String content;
    private Long senderId;
    private Long receiverId;
    private Long chatId;
}
