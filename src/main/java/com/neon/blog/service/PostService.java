package com.neon.blog.service;

import com.neon.blog.dto.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    List<PostDto> getAllPosts();

    PostDto getPostById(Long postId);

    PostDto updatePostById(Long postId,PostDto postDto);

    void deletePost(Long postId);
}
