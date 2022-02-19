package com.neon.blog.service.impl;

import com.neon.blog.dto.ChatDto;
import com.neon.blog.dto.MessageResponseDto;
import com.neon.blog.exception.BlogAPIException;
import com.neon.blog.exception.ResourceNotFoundException;
import com.neon.blog.model.Chat;
import com.neon.blog.model.User;
import com.neon.blog.repository.ChatRepositroy;
import com.neon.blog.repository.MessageRepository;
import com.neon.blog.repository.UserRepository;
import com.neon.blog.service.AuthService;
import com.neon.blog.service.ChatService;
import com.neon.blog.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {
    private ChatRepositroy chatRepositroy;
    private UserRepository userRepository;
    private MessageRepository messageRepository;
    private AuthService authService;
    private MessageService messageService;

    @Override
    public MessageResponseDto processMessage(String chatMessage, Long senderId, Long recipientId) {
        //find if chat already exists between 2
        Chat chat = chatRepositroy.getChatIdIfExists(senderId, recipientId);
        User recipientA = findUser(senderId);
        User recipientB = findUser(recipientId);

        if (chat != null) {
            //save message
            return messageService.saveMessageAndGetMessageResponseDto(chatMessage, chat, recipientA, recipientB);
        } else {
            //new chat between 2

            Chat newChat = new Chat();
            newChat.setParticipantA(recipientA);
            newChat.setParticipantB(recipientB);

            Chat savedChat = chatRepositroy.save(newChat);

            //save message
            return messageService.saveMessageAndGetMessageResponseDto(chatMessage, savedChat, recipientA, recipientB);

        }
    }


    @Override
    public List<ChatDto> getAllChats() {
        User currentUser = authService.getCurrentUser();
        List<Chat> chats = chatRepositroy.findChatsOfUser(currentUser.getId());

        List<ChatDto> chatDtos = chats.stream().map(chat -> mapChatToChatDto(chat, currentUser.getId())).collect(Collectors.toList());

        return chatDtos;
    }

    @Override
    public ChatDto createChat(Long recipientId) {
        User currentUser = authService.getCurrentUser();
        //find if chat already exists between 2
        Chat chat = chatRepositroy.getChatIdIfExists(currentUser.getId(), recipientId);

        if (chat == null) {
            //no chat exists,create one
            User recipientUser = findUser(recipientId);

            Chat newChat = new Chat();
            newChat.setParticipantA(currentUser);
            newChat.setParticipantB(recipientUser);

            Chat savedChat = chatRepositroy.save(newChat);
            return mapChatToChatDto(savedChat, currentUser.getId());
        }

        throw new BlogAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Chat already exists");
    }

    private ChatDto mapChatToChatDto(Chat chat, Long currentUserId) {
        ChatDto chatDto = new ChatDto();
        if (chat.getParticipantA().getId() == currentUserId) {
            chatDto.setRecipientId(chat.getParticipantB().getId());
            chatDto.setRecipientName(chat.getParticipantB().getName());
        } else {
            chatDto.setRecipientId(chat.getParticipantA().getId());
            chatDto.setRecipientName(chat.getParticipantA().getName());
        }

        chatDto.setChatId(chat.getId());

        //get last message of particular chat
        String message = messageRepository.findMessageByChatIdOrderByIdAsc(chat.getId());
        chatDto.setLastMessage(message);
        return chatDto;
    }

    private User findUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return user;
    }
}
