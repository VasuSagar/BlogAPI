package com.neon.blog.service.impl;

import com.neon.blog.dto.ProfileDto;
import com.neon.blog.exception.ResourceNotFoundException;
import com.neon.blog.model.User;
import com.neon.blog.repository.UserRepository;
import com.neon.blog.service.PostService;
import com.neon.blog.service.ProfileService;
import com.neon.blog.utils.AppConstraints;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private UserRepository userRepository;

    @Override
    public ProfileDto getProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return mapUserToProfileDto(user);
    }

    @Override
    public List<ProfileDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<ProfileDto> profileDtos = users.stream().map(user -> mapUserToProfileDto(user)).collect(Collectors.toList());

        return profileDtos;
    }

    private ProfileDto mapUserToProfileDto(User user) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUserName(user.getName());
        profileDto.setUserId(user.getId());
        return profileDto;
    }
}
