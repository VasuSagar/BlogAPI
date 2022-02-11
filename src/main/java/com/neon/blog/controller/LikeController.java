package com.neon.blog.controller;

import com.neon.blog.dto.CommentDto;
import com.neon.blog.dto.LikeDto;
import com.neon.blog.service.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {
    private LikeService likeService;

    @PostMapping("/{postId}")
    public ResponseEntity<LikeDto> likePost(@PathVariable(value ="postId")Long postId){
        return new ResponseEntity<>(likeService.likeOrDislikePost(postId), HttpStatus.CREATED);
    }
}
