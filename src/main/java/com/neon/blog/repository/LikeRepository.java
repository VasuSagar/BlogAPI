package com.neon.blog.repository;

import com.neon.blog.model.Comment;
import com.neon.blog.model.Like;
import com.neon.blog.model.Post;
import com.neon.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LikeRepository extends JpaRepository<Like,Long> {
    //Set<Like> findByPostId(Long postId);

    //check if user has liked post
    Optional<Like> findTopByPostAndUser(Post post, User user);

}
