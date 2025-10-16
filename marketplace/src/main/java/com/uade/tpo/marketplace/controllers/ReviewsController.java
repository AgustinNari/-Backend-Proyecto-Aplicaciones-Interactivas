package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.controllers.auth.CurrentUserProvider;
import com.uade.tpo.marketplace.entity.dto.create.ReviewCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.ReviewDeletionResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.ReviewResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ReviewUpdateDto;
import com.uade.tpo.marketplace.exceptions.ProductNotFoundException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.exceptions.UserNotFoundException;
import com.uade.tpo.marketplace.service.interfaces.IReviewService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/reviews")

public class ReviewsController {
    @Autowired
    private IReviewService reviewService;
    @Autowired
    private CurrentUserProvider authenticator;


    @GetMapping
    public ResponseEntity<Page<ReviewResponseDto>> getAllReviews(Pageable pageable) {
        Page<ReviewResponseDto> page = reviewService.getAllReviews(pageable);
        return ResponseEntity.ok(page);
    }


    @GetMapping("/me")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByUser(Pageable pageable, Authentication authentication)
            throws UserNotFoundException {
        Long userId = authenticator.getCurrentUserId(authentication);
        Page<ReviewResponseDto> page = reviewService.getReviewsByUser(userId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByProduct(@PathVariable("productId") Long productId,
                                                                       Pageable pageable)
            throws ProductNotFoundException {
        Page<ReviewResponseDto> page = reviewService.getReviewsByProduct(productId, pageable);
        return ResponseEntity.ok(page);
    }


    @GetMapping("/product/{productId}/stats")
    public ResponseEntity<Map<String, Object>> getAverageRatingAndCountByProduct(@PathVariable("productId") Long productId)
            throws ProductNotFoundException {
        var pair = reviewService.getAverageRatingAndCountByProduct(productId);
        double avg = pair.getFirst();
        long count = pair.getSecond();
        return ResponseEntity.ok(Map.of("average", avg, "count", count));
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@Valid @RequestBody ReviewCreateDto dto, Authentication authentication) {
        Long buyerId = authenticator.getCurrentUserId(authentication);
        ReviewResponseDto created = reviewService.createReview(dto, buyerId);
        return ResponseEntity.created(URI.create("/reviews/" + created.id())).body(created);
    }


    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable("reviewId") Long reviewId,
                                                          @Valid @RequestBody ReviewUpdateDto dto,
                                                          Authentication authentication) {
        Long requestingUserId = authenticator.getCurrentUserId(authentication);
        ReviewResponseDto updated = reviewService.updateReview(reviewId, dto, requestingUserId);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewDeletionResponseDto> deleteReview(@PathVariable("reviewId") Long reviewId, Authentication authentication) {
        Long requestingUserId = authenticator.getCurrentUserId(authentication);
        ReviewDeletionResponseDto deleted = reviewService.deleteReview(reviewId, requestingUserId);
        
        return ResponseEntity.ok(deleted);
    }

    @PatchMapping("/{reviewId}/visibility")
    public ResponseEntity<ReviewResponseDto> toggleReviewVisibility(
        @PathVariable("reviewId") Long reviewId,
        @RequestParam("visible") boolean visible,
        Authentication authentication)
        throws ResourceNotFoundException, UnauthorizedException {

    Long requestingUserId = authenticator.getCurrentUserId(authentication);
    ReviewResponseDto updated = reviewService.toggleReviewVisibility(reviewId, visible, requestingUserId);
    return ResponseEntity.ok(updated);
}
}
