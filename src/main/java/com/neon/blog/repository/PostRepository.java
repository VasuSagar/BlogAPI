package com.neon.blog.repository;

import com.neon.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findAllByUserId(Pageable pageable,Long userId);
}
