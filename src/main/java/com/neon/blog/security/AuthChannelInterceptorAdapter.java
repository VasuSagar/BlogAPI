package com.neon.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {
    private static final String PASSWORD_HEADER = "test";
    @Autowired
    private WebSocketAuthenticationService webSocketAuthenticationService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel)throws AuthenticationException {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
             String password = accessor.getFirstNativeHeader(PASSWORD_HEADER);
            System.out.println(password);
            final UsernamePasswordAuthenticationToken user = webSocketAuthenticationService.getAuthenticatedOrFail(password);

            accessor.setUser(user);
        }
        return message;
    }
}
