package com.neon.blog.service;

import com.neon.blog.dto.PostDto;
import com.neon.blog.dto.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    PostResponse getAllPosts(int pageNo, int pageSize);

    PostDto getPostById(Long postId);

    PostDto updatePostById(Long postId,PostDto postDto);

    void deletePost(Long postId);
}
