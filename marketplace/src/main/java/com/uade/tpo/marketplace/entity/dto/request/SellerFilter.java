package com.uade.tpo.marketplace.entity.dto.request;

public class SellerFilter {
    private Double minAvgRating;
    private Long minAmountSold;


    public Double getMinAvgRating() { return minAvgRating; }
    public void setMinAvgRating(Double minAvgRating) { this.minAvgRating = minAvgRating; }

    public Long getMinAmountSold() { return minAmountSold; }
    public void setMinAmountSold(Long minAmountSold) { this.minAmountSold = minAmountSold; }
}
