package com.uade.tpo.marketplace.entity.dto.response;

public class SellerListDTO {
    private Long id;
    private String displayName;


    private String avatarContentType;
    private String avatarDataUrl;


    private Double avgRating;
    private Long ratingCount;
    private Long soldKeys;
    private Long amountSold;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getAvatarContentType() { return avatarContentType; }
    public void setAvatarContentType(String avatarContentType) { this.avatarContentType = avatarContentType; }

    public String getAvatarDataUrl() { return avatarDataUrl; }
    public void setAvatarDataUrl(String avatarDataUrl) { this.avatarDataUrl = avatarDataUrl; }

    public Double getAvgRating() { return avgRating; }
    public void setAvgRating(Double avgRating) { this.avgRating = avgRating; }

    public Long getRatingCount() { return ratingCount; }
    public void setRatingCount(Long ratingCount) { this.ratingCount = ratingCount; }

    public Long getSoldKeys() { return soldKeys; }
    public void setSoldKeys(Long soldKeys) { this.soldKeys = soldKeys; }

    public Long getAmountSold() { return amountSold; }
    public void setAmountSold(Long amountSold) { this.amountSold = amountSold; }
}
