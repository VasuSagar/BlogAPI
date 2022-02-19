package com.neon.blog.repository;

import com.neon.blog.model.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepositroy extends JpaRepository<Chat,Long> {
    @Query(value = "select * from chats c where c.participant_a in (:senderId,:recipientId) and c.participant_b in (:senderId,:recipientId)" ,nativeQuery = true)
    Chat getChatIdIfExists(Long senderId, Long recipientId);

    @Query(value = "select * from chats c where c.participant_a=?1 or c.participant_b=?1",nativeQuery = true)
    List<Chat> findChatsOfUser(Long id);

}
