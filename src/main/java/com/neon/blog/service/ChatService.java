package com.neon.blog.service;

import com.neon.blog.dto.ChatDto;
import com.neon.blog.dto.MessageResponseDto;

import java.util.List;

public interface ChatService {
    MessageResponseDto processMessage(String message, Long senderId, Long recipientId);

    List<ChatDto> getAllChats();

    ChatDto createChat(Long recipientId);
}
