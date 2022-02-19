package com.neon.blog.service;

import com.neon.blog.dto.MessageResponseDto;
import com.neon.blog.dto.MessageResponsePagableDto;
import com.neon.blog.model.Chat;
import com.neon.blog.model.User;

import java.util.List;

public interface MessageService {
    MessageResponsePagableDto getAllMessagesByChatId(Long chatId, int pageNo, int pageSize);

    MessageResponseDto saveMessageAndGetMessageResponseDto(String chatMessage, Chat chat, User recipientA, User recipientB);
}
