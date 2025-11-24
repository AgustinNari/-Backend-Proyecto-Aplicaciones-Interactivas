package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;

public record AdminStatsExtrasResponseDto(

    Integer totalOrders,
    Integer ordersToday,
    Integer totalReviews,
    BigDecimal totalRevenue

) {

}
