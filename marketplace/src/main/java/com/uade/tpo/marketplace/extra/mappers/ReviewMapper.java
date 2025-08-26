package com.uade.tpo.marketplace.extra.mappers;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.OrderItem;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.basic.Review;
import com.uade.tpo.marketplace.entity.dto.create.ReviewCreateDto;
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
        Long productId = r.getProduct() != null ? r.getProduct().getId() : null;
        Long buyerId = r.getBuyer() != null ? r.getBuyer().getId() : null;
        return new ReviewResponseDto(r.getId(), productId, buyerId, r.getRating(), r.getTitle(), r.getComment(), r.isVisible(), r.getCreatedAt());
    }
}