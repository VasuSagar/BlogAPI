package com.neon.blog.service.impl;

import com.neon.blog.dto.CommentDto;
import com.neon.blog.exception.BlogAPIException;
import com.neon.blog.exception.ResourceNotFoundException;
import com.neon.blog.model.Comment;
import com.neon.blog.model.Post;
import com.neon.blog.repository.CommentRepository;
import com.neon.blog.repository.PostRepository;
import com.neon.blog.service.AuthService;
import com.neon.blog.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private AuthService authService;

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Post post=findPost(postId);
        Comment comment=mapToEntity(commentDto);
        comment.setPost(post);

       Comment savedComment=commentRepository.save(comment);

       return mapToDTO(savedComment);

    }

    @Override
    public List<CommentDto> getAllCommentsByPostId(Long postId) {
        //get post if no posts exists then throw exception
        findPost(postId);

        List<Comment> comments=commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentByPostIdAndCommentId(Long postId, Long commentId) {
        //get post if no posts exists then throw exception
        Post post=findPost(postId);

        //get comment if no comment exists then throw exception
        Comment comment=findComment(commentId);

        //check if retrieved comment is of retrieved post
        if(comment.getPost().getId()!=postId){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment Does not belongs to Post");
        }
        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto) {
        //get post if no posts exists then throw exception
        findPost(postId);

        //get comment if no comment exists then throw exception
        Comment comment=findComment(commentId);

        //check if retrieved comment is of retrieved post
        if(comment.getPost().getId()!=postId){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment Does not belongs to Post");
        }

        //update comment
        comment.setBody(commentDto.getBody());

        Comment savedComment=commentRepository.save(comment);

        return mapToDTO(savedComment);

    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        //get post if no posts exists then throw exception
        Post post=findPost(postId);

        //get comment if no comment exists then throw exception
        Comment comment=findComment(commentId);

        //check if retrieved comment is of retrieved post
        if(comment.getPost().getId()!=postId){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment Does not belongs to Post");
        }

        //delete comment
        commentRepository.delete(comment);

    }

    private Comment findComment(Long commentId) {
        Comment comment=commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment","id",commentId));
        return comment;
    }

    private Post findPost(Long postId){
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        return post;
    }

    private CommentDto mapToDTO(Comment comment) {
        //convert entity to DTO
        CommentDto commentDto=new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setBody(comment.getBody());
        commentDto.setUserId(comment.getUser().getId());
        commentDto.setUserName(comment.getUser().getName());
        commentDto.setCreatedDate(comment.getCreatedDate());

        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        //convert DTO to entity
        Comment comment=new Comment();
        comment.setBody(commentDto.getBody());
        comment.setUser(authService.getCurrentUser());
        comment.setCreatedDate(Instant.now());

        return comment;
    }
}
