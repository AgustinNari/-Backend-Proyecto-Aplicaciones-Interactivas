package com.uade.tpo.marketplace.extra.mappers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.OrderItem;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.basic.Review;
import com.uade.tpo.marketplace.entity.dto.create.ReviewCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.LatestReviewResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.ReviewResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ReviewUpdateDto;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewCreateDto dto){
        if (dto == null) return null;
        Review r = new Review();
        if (dto.productId() != null) {
            Product p = new Product();
            p.setId(dto.productId());
            r.setProduct(p);
        }
        if (dto.orderItemId() != null) {
            OrderItem oi = new OrderItem();
            oi.setId(dto.orderItemId());
            r.setOrderItem(oi);
        }
        r.setRating(dto.rating());
        r.setTitle(dto.title());
        r.setComment(dto.comment());
        return r;
    }

    public void updateFromDto(ReviewUpdateDto dto, Review entity){
        if (dto == null || entity == null) return;
        if (dto.rating() != null) entity.setRating(dto.rating());
        if (dto.title() != null) entity.setTitle(dto.title());
        if (dto.comment() != null) entity.setComment(dto.comment());
        if (dto.visible() != null) entity.setVisible(dto.visible());
    }


    public ReviewResponseDto toResponse(Review r){
        if (r == null) return null;
        Long productId = r.getProduct() != null ? safeGetId(r.getProduct()) : null;
        Long buyerId = r.getBuyer() != null ? safeGetId(r.getBuyer()) : null;
        return new ReviewResponseDto(
            r.getId(),
            productId,
            buyerId,
            r.getRating(),
            r.getTitle(),
            r.getComment(),
            r.isVisible(),
            r.getCreatedAt()
        );
    }


    public List<ReviewResponseDto> toResponseList(Collection<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) return Collections.emptyList();
        return reviews.stream().filter(Objects::nonNull).map(this::toResponse).collect(Collectors.toList());
    }


    public Review fromId(Long id) {
        if (id == null) return null;
        Review r = new Review();
        r.setId(id);
        return r;
    }

    private Long safeGetId(Product p) {
        try { return p.getId(); } catch (Exception e) { return null; }
    }

    private Long safeGetId(com.uade.tpo.marketplace.entity.basic.User u) {
        try { return u.getId(); } catch (Exception e) { return null; }
    }

// En ReviewMapper.java agregar este método:

public LatestReviewResponseDto toLatestReviewResponse(Review review, 
                                                    String productTitle, 
                                                    String productImageDataUrl,
                                                    String buyerDisplayName) {
    if (review == null) return null;
    
    return new LatestReviewResponseDto(
        review.getId(),
        review.getRating(),
        review.getTitle(),
        review.getComment(),
        review.getCreatedAt(),
        review.getProduct() != null ? safeGetId(review.getProduct()) : null,
        productTitle,
        productImageDataUrl,
        review.getBuyer() != null ? safeGetId(review.getBuyer()) : null,
        buyerDisplayName
    );
}


}