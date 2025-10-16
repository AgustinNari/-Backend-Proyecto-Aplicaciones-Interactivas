package com.uade.tpo.marketplace.entity.dto.response;

public record UserAvatarResponseDto(
    Long userId,
    String contentType,
    String base64File,
    String dataUrl
) {

}
