package com.neon.blog.service.impl;

import com.neon.blog.dto.LikeDto;
import com.neon.blog.exception.ResourceNotFoundException;
import com.neon.blog.model.Like;
import com.neon.blog.model.Post;
import com.neon.blog.model.User;
import com.neon.blog.repository.LikeRepository;
import com.neon.blog.repository.PostRepository;
import com.neon.blog.service.AuthService;
import com.neon.blog.service.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {
    private LikeRepository likeRepository;
    private PostRepository postRepository;
    private AuthService authService;

    @Override
    public LikeDto likeOrDislikePost(Long postId) {
        //check if post exists
        Post post=findPost(postId);

        //check if post is not liked by user
        Optional<Like> optionalLike= likeRepository.findTopByPostAndUser(post,authService.getCurrentUser());

        if(optionalLike.isPresent()){
            return dislikePost(post,optionalLike.get());
        }
        else{
            return likePost(post,authService.getCurrentUser());
        }
    }

    private LikeDto dislikePost(Post post, Like like) {
        likeRepository.delete(like);

        //decrease like count
        post.setLikeCount(post.getLikeCount()-1);
        postRepository.save(post);
        return new LikeDto();
    }

    private LikeDto likePost(Post post, User currentUser) {
        Like like=new Like();
        like.setPost(post);
        like.setUser(currentUser);
        //persist
       Like like1=likeRepository.save(like);

        //increase post count
        post.setLikeCount(post.getLikeCount()+1);
        postRepository.save(post);

        //map like to likeDto
        LikeDto likeDto=new LikeDto();
        likeDto.setUserName(like1.getUser().getName());
        likeDto.setId(like.getId());
        likeDto.setUserId(like1.getUser().getId());

        return likeDto;
    }

    private Post findPost(Long postId){
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        return post;
    }
}
