package com.neon.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
public class LikeDto {
    private Long id;
    private String userName;
    private Long userId;
}
