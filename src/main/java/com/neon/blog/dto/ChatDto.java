package com.neon.blog.dto;

import lombok.Data;

@Data
public class ChatDto {
    private Long chatId;
    private Long recipientId;
    private String recipientName;
    private String lastMessage;
}
