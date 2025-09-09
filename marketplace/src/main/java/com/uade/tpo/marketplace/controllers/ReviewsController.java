package com.uade.tpo.marketplace.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.dto.create.ReviewCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.ReviewResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ReviewUpdateDto;
import com.uade.tpo.marketplace.exceptions.ProductNotFoundException;
import com.uade.tpo.marketplace.exceptions.ReviewNotFoundException;
import com.uade.tpo.marketplace.exceptions.UserNotFoundException;
import com.uade.tpo.marketplace.service.interfaces.IReviewService;


@RestController
@RequestMapping("/reviews")

public class ReviewsController {
    @Autowired
    private IReviewService reviewService;

    @GetMapping("{reviewId}")
    public Page<ReviewResponseDto> getReviewByUser(@PathVariable Long userId, Pageable pageable)
            throws UserNotFoundException {
        Page<ReviewResponseDto> result = reviewService.getReviewsByUser(userId, pageable);
        return result;
    }

    @GetMapping("{reviewId}")
    public Page<ReviewResponseDto> getReviewByProduct(@PathVariable Long productId, Pageable pageable, boolean onlyVisible)
            throws ProductNotFoundException {
        Page<ReviewResponseDto> result = reviewService.getReviewsByProduct(productId, pageable, onlyVisible);
        return result;
    }

    @GetMapping("{reviewId}")
    public Page<ReviewResponseDto> getAllReviews(@PathVariable Pageable pageable)
            throws ReviewNotFoundException {
        Page<ReviewResponseDto> result = reviewService.getAllReviews(pageable);
        return result;
    }

    @GetMapping("{reviewId}")
    public Page<ReviewResponseDto> getAverageRatingAndCountByProduct(@PathVariable Long productId, Pageable pageable)
            throws ProductNotFoundException, ReviewNotFoundException {
        Page<ReviewResponseDto> result = reviewService.getReviewsByUser(productId, pageable);
        return result;
    }

    @PostMapping()
    public ResponseEntity<ReviewResponseDto> createReview( @RequestBody ReviewCreateDto reviewCreateDto, Long buyerId){
        ReviewResponseDto result = reviewService.createReview(reviewCreateDto, buyerId);
        return ResponseEntity.created(URI.create("reviews/" + result.id())).body(result);
    }

    @PostMapping()
    public ResponseEntity<ReviewResponseDto> updateReview( @RequestBody ReviewUpdateDto reviewUpdateDto, Long reviewId, Long requestingUserId){
        ReviewResponseDto result = reviewService.updateReview(reviewId, reviewUpdateDto, requestingUserId);
        return ResponseEntity.ok(result);
    }

    @PostMapping()
    public void deleteReview(Long reviewId, Long requestingUserId){
        reviewService.deleteReview(reviewId, requestingUserId);
    }
}//Enrique Busso
