package com.neon.blog.service;

import com.neon.blog.dto.LikeDto;

public interface LikeService {
    LikeDto likeOrDislikePost(Long postId);
}
