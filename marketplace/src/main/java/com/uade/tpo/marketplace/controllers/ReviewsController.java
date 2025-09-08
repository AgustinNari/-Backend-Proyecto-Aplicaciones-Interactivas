package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.basic.Review;
import com.uade.tpo.marketplace.entity.dto.create.ReviewCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.ReviewResponseDto;
import com.uade.tpo.marketplace.exceptions.ReviewNotFoundException;
import com.uade.tpo.marketplace.exceptions.ProductNotFoundException;
import com.uade.tpo.marketplace.exceptions.UserNotFoundException;
import com.uade.tpo.marketplace.service.interfaces.IReviewService;


@RestController
@RequestMapping("/reviews")

public class ReviewsController {
    @Autowired
    private IReviewService reviewService;

    @GetMapping("{reviewId}")
    public ResponseEntity<Review> getReviewByUser(@PathVariable Long userId, Pageable pageable)
            throws UserNotFoundException {
        Optional<Review> result = reviewService.getReviewsByUser(userId, pageable);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{reviewId}")
    public ResponseEntity<Review> getReviewByProduct(@PathVariable Long productId, Pageable pageable, boolean onlyVisible)
            throws ProductNotFoundException {
        Optional<Review> result = reviewService.getReviewsByProduct(productId, pageable, onlyVisible);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{reviewId}")
    public ResponseEntity<Review> getAllReviews(@PathVariable Pageable pageable)
            throws ReviewNotFoundException {
        Optional<Review> result = reviewService.getReviewsByUser(pageable);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{reviewId}")
    public ResponseEntity<Review> getAverageRatingAndCountByProduct(@PathVariable Long productId)
            throws ProductNotFoundException, ReviewNotFoundException {
        Optional<Review> result = reviewService.getReviewsByUser(productId);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<Review> createReview(@Valid @RequestBody ReviewCreateDto reviewCreateDto, Long buyerId){
        Review result = reviewService.createReview(reviewCreateDto.title(), reviewCreateDto.comment(), buyerId);
        return ResponseEntity.created(URI.create("reviews/" + result.getId())).body(result);
    }

    @PostMapping()
    public ResponseEntity<Review> updateReview(@Valid Long reviewId, ReviewUpdateDto reviewUpdateDto, Long requestingUserId){
        Review result = reviewService.updateReview(reviewId, reviewUpdateDto, requestingUserId);
        return ResponseEntity.ok(result);
    }

    @PostMapping()
    public ResponseEntity<Review> deleteReview(@Valid Long reviewId, Long requestingUserId){
        reviewService.deleteReview(reviewId, requestingUserId)
        return ResponseEntity.noContent().build();
    }
}//Enrique Busso
