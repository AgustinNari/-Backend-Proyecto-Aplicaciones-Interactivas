package com.uade.tpo.marketplace.entity.dto.request;

import java.util.List;

public class SellerFilter {
    private List<Long> sellerIds;
    private Double minAvgRating;
    private Long minAmountSold;

    public List<Long> getSellerIds() { return sellerIds; }
    public void setSellerIds(List<Long> sellerIds) { this.sellerIds = sellerIds; }

    public Double getMinAvgRating() { return minAvgRating; }
    public void setMinAvgRating(Double minAvgRating) { this.minAvgRating = minAvgRating; }

    public Long getMinAmountSold() { return minAmountSold; }
    public void setMinAmountSold(Long minAmountSold) { this.minAmountSold = minAmountSold; }
}
