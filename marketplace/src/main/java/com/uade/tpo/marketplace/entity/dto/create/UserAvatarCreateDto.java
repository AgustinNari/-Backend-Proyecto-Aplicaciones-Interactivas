package com.uade.tpo.marketplace.entity.dto.create;

import org.springframework.web.multipart.MultipartFile;

public record UserAvatarCreateDto(
    Long userId,
    MultipartFile file
) {
}
