package com.uade.tpo.marketplace.entity.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record ProductFilterRequestDto(
    List<String> categories,
    BigDecimal priceMin,
    BigDecimal priceMax,
    String platform,
    String region,
    Boolean onlyActive
) {

}
