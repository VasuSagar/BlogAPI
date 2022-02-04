package com.neon.blog.controller;

import com.neon.blog.dto.PostDto;
import com.neon.blog.dto.PostResponse;
import com.neon.blog.service.PostService;
import com.neon.blog.utils.AppConstraints;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private PostService postService;

    @PostMapping()
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNo",defaultValue = AppConstraints.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize",defaultValue =AppConstraints.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstraints.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstraints.DEFAULT_SORT_DIRECTION)String sortDir){
        return new ResponseEntity<>(postService.getAllPosts(pageNo,pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name ="id") Long postId){
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePostById(@PathVariable(name = "id")Long postId,@Valid @RequestBody PostDto postDto){
        PostDto postResponse=postService.updatePostById(postId,postDto);
        return new ResponseEntity<>(postResponse,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id")Long postId){
        postService.deletePost(postId);
        return new ResponseEntity<>("Post Deleted Successfully",HttpStatus.OK);
    }

}
