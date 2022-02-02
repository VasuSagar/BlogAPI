package com.neon.blog.service;

import com.neon.blog.dto.PostDto;
import com.neon.blog.dto.PostResponse;

public interface PostService {
    PostDto createPost(PostDto postDto);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostById(Long postId);

    PostDto updatePostById(Long postId,PostDto postDto);

    void deletePost(Long postId);
}
