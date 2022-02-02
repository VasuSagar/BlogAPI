package com.neon.blog.service.impl;

import com.neon.blog.dto.PostDto;
import com.neon.blog.dto.PostResponse;
import com.neon.blog.exception.ResourceNotFoundException;
import com.neon.blog.model.Post;
import com.neon.blog.repository.PostRepository;
import com.neon.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    @Override
    public PostDto createPost(PostDto postDto) {

        //convert DTO to entity
        Post post=mapToEntity(postDto);
        Post post1=postRepository.save(post);

        //convert entity to DTO
        PostDto postDto1=mapToDTO(post1);
        return postDto1;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize) {
        //create pagable instance
        Pageable pageable= PageRequest.of(pageNo,pageSize);

        Page<Post> posts=postRepository.findAll(pageable);

        //get content for page object
        List<Post> postList=posts.getContent();

        List<PostDto> postDtos=postList.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        //create PostResponse from postDto
        return mapTOPagablePostResponse(postDtos,posts);

    }

    private PostResponse mapTOPagablePostResponse(List<PostDto> postDtos, Page<Post> posts) {
        PostResponse postResponse=new PostResponse();
        postResponse.setPosts(postDtos);
        postResponse.setLast(posts.isLast());
        postResponse.setCurrentPage(posts.getNumber());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setPageSize(posts.getSize());
        return postResponse;
    }

    @Override
    public PostDto getPostById(Long postId) {
        Post post=findPost(postId);
       return mapToDTO(post);
    }

    @Override
    public PostDto updatePostById(Long postId, PostDto postDto) {
        Post post=findPost(postId);
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());

        Post updatedPost=postRepository.save(post);

        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePost(Long postId) {
        Post post=findPost(postId);
        postRepository.delete(post);
    }

    private PostDto mapToDTO(Post post1) {
        //convert entity to DTO
        PostDto postDto1=new PostDto();
        postDto1.setId(post1.getId());
        postDto1.setDescription(post1.getDescription());
        postDto1.setContent(post1.getContent());
        postDto1.setTitle(post1.getTitle());
        return postDto1;
    }

    private Post mapToEntity(PostDto postDto){
        //convert DTO to entity
        Post post=new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        return post;
    }

    private Post findPost(Long postId){
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        return post;
    }
}
