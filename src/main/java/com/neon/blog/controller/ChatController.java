package com.neon.blog.controller;

import com.neon.blog.dto.ChatDto;
import com.neon.blog.dto.ChatMessageRequest;
import com.neon.blog.dto.MessageResponseDto;
import com.neon.blog.dto.MessageResponsePagableDto;
import com.neon.blog.service.ChatService;
import com.neon.blog.service.MessageService;
import com.neon.blog.utils.AppConstraints;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class ChatController {
    private SimpMessagingTemplate simpMessagingTemplate;
    private ChatService chatService;
    private MessageService messageService;

    @MessageMapping("/message/{recipientId}")
    public void processMessage(@DestinationVariable Long recipientId, ChatMessageRequest chatMessageRequest) {
        MessageResponseDto messageResponseDto= chatService.processMessage(chatMessageRequest.getMessage(),chatMessageRequest.getSenderId(),recipientId);

        simpMessagingTemplate.convertAndSend("/topic/reply/"+recipientId,messageResponseDto);
    }

    @PostMapping("/api/chats")
    public ResponseEntity<List<ChatDto>> getAllChatsOfLoggedInUser (){
        return new ResponseEntity<>(chatService.getAllChats(), HttpStatus.OK);
    }


    @PostMapping("/api/chats/all/{id}")
    public ResponseEntity<MessageResponsePagableDto> getAllMessagesByChatId(@PathVariable(name ="id") Long chatId,
                                                                                  @RequestParam(value = "pageNo",defaultValue = AppConstraints.DEFAULT_PAGE_NUMBER) int pageNo,
                                                                                  @RequestParam(value = "pageSize",defaultValue =AppConstraints.DEFAULT_PAGE_SIZE) int pageSize){
        return ResponseEntity.ok(messageService.getAllMessagesByChatId(chatId,pageNo,pageSize));
    }

    @PostMapping("/api/chats/{id}")
    public ResponseEntity<ChatDto> createChat(@PathVariable(name ="id") Long recipientId){
        return ResponseEntity.ok(chatService.createChat(recipientId));
    }
}
