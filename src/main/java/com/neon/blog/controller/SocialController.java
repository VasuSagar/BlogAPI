package com.neon.blog.controller;

import com.neon.blog.dto.PostDto;
import com.neon.blog.dto.ProfileDto;
import com.neon.blog.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/profile")
public class SocialController {
    private ProfileService profileService;

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable(name ="userId") Long userId){
        return new ResponseEntity<>(profileService.getProfile(userId), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<ProfileDto>> getAllUsers(){
        return new ResponseEntity<>(profileService.getAllUsers(), HttpStatus.OK);
    }

}
