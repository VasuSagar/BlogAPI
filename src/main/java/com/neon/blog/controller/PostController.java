package com.neon.blog.controller;

import com.neon.blog.dto.PostDto;
import com.neon.blog.dto.PostResponse;
import com.neon.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private PostService postService;

    @PostMapping()
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<PostResponse> getAllPosts(@RequestParam(value = "pageNo",defaultValue = "0") int pageNo, @RequestParam(value = "pageSize",defaultValue ="5") int pageSize){
        return new ResponseEntity<>(postService.getAllPosts(pageNo,pageSize),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name ="id") Long postId){
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePostById(@PathVariable(name = "id")Long postId,@RequestBody PostDto postDto){
        PostDto postResponse=postService.updatePostById(postId,postDto);
        return new ResponseEntity<>(postResponse,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id")Long postId){
        postService.deletePost(postId);
        return new ResponseEntity<>("Post Deleted Successfully",HttpStatus.OK);
    }

}
