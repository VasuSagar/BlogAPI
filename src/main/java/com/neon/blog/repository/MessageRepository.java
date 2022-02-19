package com.neon.blog.repository;

import com.neon.blog.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    @Query(value = "select m.content from messages m where m.chat_id=?1 order by m.id desc limit 1",nativeQuery = true)
    String findMessageByChatIdOrderByIdAsc(Long chatId);

    Page<Message> findAllByChatId(Long chatId, Pageable pageable);
}
