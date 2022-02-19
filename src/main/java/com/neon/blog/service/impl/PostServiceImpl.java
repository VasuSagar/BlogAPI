package com.neon.blog.service.impl;

import com.neon.blog.dto.CommentDto;
import com.neon.blog.dto.LikeDto;
import com.neon.blog.dto.PostDto;
import com.neon.blog.dto.PostResponse;
import com.neon.blog.exception.ResourceNotFoundException;
import com.neon.blog.model.Comment;
import com.neon.blog.model.Like;
import com.neon.blog.model.Post;
import com.neon.blog.repository.PostRepository;
import com.neon.blog.repository.UserRepository;
import com.neon.blog.service.AuthService;
import com.neon.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private AuthService authService;
    private UserRepository userRepository;

    @Override
    public PostDto createPost(PostDto postDto) {

        //convert DTO to entity
        Post post=mapToEntity(postDto);
        Post savedPost=postRepository.save(post);

        //convert entity to DTO
        PostDto postDto1=mapToDTO(savedPost);
        return postDto1;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir, Long userId) {
        //create Sort object
        Sort sort=createSortObject(sortDir,sortBy);

        //create pagable instance
        Pageable pageable= PageRequest.of(pageNo,pageSize, sort);
        Page<Post> posts;
        //return all posts if userId is default(0l)
        if(userId==0){
            posts=postRepository.findAll(pageable);
        }
        else{
            //check if user exists
            userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",userId));
            posts=postRepository.findAllByUserId(pageable,userId);
        }


        //get content for page object
        List<Post> postList=posts.getContent();

        List<PostDto> postDtos=postList.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        //create PostResponse from postDto
        return mapTOPagablePostResponse(postDtos,posts);

    }

    private Sort createSortObject(String sortDir, String sortBy) {
        Sort sort;
        if(sortDir.equalsIgnoreCase("asc")){
            sort=Sort.by(sortBy).ascending();
        }
        else if(sortDir.equalsIgnoreCase("desc")){
            sort=Sort.by(sortBy).descending();
        }
        else
            throw new IllegalArgumentException("Specify sort direction correctly");
        return sort;
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
        postDto1.setCreatedDate(post1.getCreatedDate());
        postDto1.setUserName(post1.getUser().getName());
        postDto1.setUserId(post1.getUser().getId());
        postDto1.setLikesCount(post1.getLikeCount());


        Set<Comment> comments= post1.getComments();

        Set<CommentDto> commentDtos=comments.stream().map(comment -> mapCommentToCommentDto(comment)).collect(Collectors.toSet());

        postDto1.setComments(commentDtos);

        Set<Like> likes= post1.getLikes();

        Set<LikeDto> likeDtos=likes.stream().map(like -> mapLikeToLikeDto(like)).collect(Collectors.toSet());

        postDto1.setLikes(likeDtos);


        return postDto1;
    }

    private LikeDto mapLikeToLikeDto(Like like) {
        LikeDto likeDto=new LikeDto();
        likeDto.setId(like.getId());
        likeDto.setUserId(like.getUser().getId());
        likeDto.setUserName(like.getUser().getName());
        return likeDto;
    }

    private CommentDto mapCommentToCommentDto(Comment comment){
        CommentDto commentDto=new CommentDto();
        commentDto.setBody(comment.getBody());
        commentDto.setId(comment.getId());
        commentDto.setCreatedDate(comment.getCreatedDate());
        commentDto.setUserId(comment.getUser().getId());
        commentDto.setUserName(comment.getUser().getName());

        return commentDto;
    }

    private Post mapToEntity(PostDto postDto){
        //convert DTO to entity
        Post post=new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        post.setUser(authService.getCurrentUser());
        post.setCreatedDate(Instant.now());
        return post;
    }

    private Post findPost(Long postId){
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        return post;
    }
}
