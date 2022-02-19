package com.neon.blog.service;

import com.neon.blog.dto.ProfileDto;

import java.util.List;

public interface ProfileService {
    ProfileDto getProfile(Long userId);

    List<ProfileDto> getAllUsers();
}
