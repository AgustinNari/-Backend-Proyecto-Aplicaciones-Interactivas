package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public class ProductListDTO {
    private Long id;
    private Long sellerId;
    private String sellerDisplayName;

    private String title;
    private BigDecimal price;
    private boolean active;
    private String platform;
    private String region;
    private LocalDate releaseDate;
    private String developer;
    private String publisher;
    private Integer metacriticScore;
    private boolean featured;


    private List<CategoryResponseDto> categories;


    private Long bestDiscountId;
    private BigDecimal bestDiscountPercentage;


    private String primaryImageDataUrl;
    private String primaryImageContentType;


    private Double avgRating;
    private Long ratingCount;
    private Integer stock;
    private Integer sold;
    private Long amountSold;




    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getSellerId() {
        return sellerId;
    }
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
    public String getSellerDisplayName() {
        return sellerDisplayName;
    }
    public void setSellerDisplayName(String sellerDisplayName) {
        this.sellerDisplayName = sellerDisplayName;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public LocalDate getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
    public String getDeveloper() {
        return developer;
    }
    public void setDeveloper(String developer) {
        this.developer = developer;
    }
    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public Integer getMetacriticScore() {
        return metacriticScore;
    }
    public void setMetacriticScore(Integer metacriticScore) {
        this.metacriticScore = metacriticScore;
    }
    public boolean isFeatured() {
        return featured;
    }
    public void setFeatured(boolean featured) {
        this.featured = featured;
    }
    public List<CategoryResponseDto> getCategories() {
        return categories;
    }
    public void setCategories(List<CategoryResponseDto> categories) {
        this.categories = categories;
    }
    public Long getBestDiscountId() {
        return bestDiscountId;
    }
    public void setBestDiscountId(Long bestDiscountId) {
        this.bestDiscountId = bestDiscountId;
    }
    public BigDecimal getBestDiscountPercentage() {
        return bestDiscountPercentage;
    }
    public void setBestDiscountPercentage(BigDecimal bestDiscountPercentage) {
        this.bestDiscountPercentage = bestDiscountPercentage;
    }
    public String getPrimaryImageDataUrl() {
        return primaryImageDataUrl;
    }
    public void setPrimaryImageDataUrl(String primaryImageDataUrl) {
        this.primaryImageDataUrl = primaryImageDataUrl;
    }
    public String getPrimaryImageContentType() {
        return primaryImageContentType;
    }
    public void setPrimaryImageContentType(String primaryImageContentType) {
        this.primaryImageContentType = primaryImageContentType;
    }
    public Double getAvgRating() {
        return avgRating;
    }
    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }
    public Long getRatingCount() {
        return ratingCount;
    }
    public void setRatingCount(Long ratingCount) {
        this.ratingCount = ratingCount;
    }
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public Integer getSold() {
        return sold;
    }
    public void setSold(Integer sold) {
        this.sold = sold;
    }
    public Long getAmountSold() {
        return amountSold;
    }
    public void setAmountSold(Long amountSold) {
        this.amountSold = amountSold;
    }




    

}