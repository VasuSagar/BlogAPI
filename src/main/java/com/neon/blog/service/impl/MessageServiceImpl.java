package com.neon.blog.service.impl;

import com.neon.blog.dto.MessageResponseDto;
import com.neon.blog.dto.MessageResponsePagableDto;
import com.neon.blog.dto.PostResponse;
import com.neon.blog.exception.ResourceNotFoundException;
import com.neon.blog.model.Chat;
import com.neon.blog.model.Message;
import com.neon.blog.model.User;
import com.neon.blog.repository.ChatRepositroy;
import com.neon.blog.repository.MessageRepository;
import com.neon.blog.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    private ChatRepositroy chatRepositroy;
    private MessageRepository messageRepository;

    @Override
    public MessageResponsePagableDto getAllMessagesByChatId(Long chatId, int pageNo, int pageSize) {
        //check if chat exists of given id
        Chat chat = chatRepositroy.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat", "id", chatId));

        //create Sort object
        //this will be updated after setting up message time
        Sort sort=Sort.by("id").descending();

        //create pagable instance
        Pageable pageable= PageRequest.of(pageNo,pageSize,sort);

        Page<Message> messages = messageRepository.findAllByChatId(chatId,pageable);

        List<Message> messageList=messages.getContent();

        List<MessageResponseDto> messageResponseDtos = messageList.stream().map(message -> mapMessageToMessageResponse(message, chatId)).collect(Collectors.toList());

        //create pagable response
        return mapToPagableMessageResponse(messageResponseDtos,messages);
    }

    private MessageResponsePagableDto mapToPagableMessageResponse(List<MessageResponseDto> messageResponseDtos, Page<Message> messages) {
        MessageResponsePagableDto messageResponsePagableDto=new MessageResponsePagableDto();
        messageResponsePagableDto.setMessages(messageResponseDtos);
        messageResponsePagableDto.setLast(messages.isLast());
        messageResponsePagableDto.setCurrentPage(messages.getNumber());
        messageResponsePagableDto.setTotalElements(messages.getTotalElements());
        messageResponsePagableDto.setTotalPages(messages.getTotalPages());
        messageResponsePagableDto.setPageSize(messages.getSize());
        return messageResponsePagableDto;

    }

    @Override
    public MessageResponseDto saveMessageAndGetMessageResponseDto(String chatMessage, Chat chat, User recipientA, User recipientB) {
        Message message = new Message();
        message.setChat(chat);
        message.setContent(chatMessage);
        message.setSenderId(recipientA);
        message.setReceiverId(recipientB);

        Message savedMessage = messageRepository.save(message);

        MessageResponseDto messageResponseDto = mapMessageToMessageResponse(savedMessage, chat.getId());

        return messageResponseDto;
    }

    private MessageResponseDto mapMessageToMessageResponse(Message message, Long chatId) {
        MessageResponseDto messageResponseDto = new MessageResponseDto();
        messageResponseDto.setMessageId(message.getId());
        messageResponseDto.setSenderId(message.getSenderId().getId());
        messageResponseDto.setReceiverId(message.getReceiverId().getId());
        messageResponseDto.setContent(message.getContent());
        messageResponseDto.setChatId(chatId);
        return messageResponseDto;
    }
}
