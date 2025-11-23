package com.uade.tpo.marketplace.entity.dto.response;

import java.util.List;

public record FilterExtrasResponseDto(
        List<String> developers,
        List<String> publishers
) {

}
