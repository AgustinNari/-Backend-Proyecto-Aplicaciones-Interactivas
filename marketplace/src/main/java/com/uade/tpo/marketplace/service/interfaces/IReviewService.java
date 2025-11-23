package com.uade.tpo.marketplace.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import com.uade.tpo.marketplace.entity.dto.create.ReviewCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.LatestReviewResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.ReviewDeletionResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.ReviewResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ReviewUpdateDto;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.DuplicateReviewException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;

public interface IReviewService {

    ReviewResponseDto createReview(ReviewCreateDto dto, Long buyerId) throws ResourceNotFoundException, BadRequestException, DuplicateReviewException;

    ReviewResponseDto updateReview(Long reviewId, ReviewUpdateDto dto, Long requestingUserId) throws ResourceNotFoundException, UnauthorizedException;

    ReviewDeletionResponseDto deleteReview(Long reviewId, Long requestingUserId) throws ResourceNotFoundException, UnauthorizedException;

    Page<ReviewResponseDto> getReviewsByProduct(Long productId, Pageable pageable) throws ResourceNotFoundException;

    Page<ReviewResponseDto> getReviewsByUser(Long userId, Pageable pageable) throws ResourceNotFoundException;

    Page<ReviewResponseDto> getAllReviews(Pageable pageable);

    Pair<Double, Long> getAverageRatingAndCountByProduct(Long productId) throws ResourceNotFoundException;

    ReviewResponseDto toggleReviewVisibility(Long reviewId, boolean visible, Long requestingUserId) throws ResourceNotFoundException, UnauthorizedException;

    ReviewResponseDto getReviewByOrderItemIdForBuyer(Long orderItemId, Long requestingUserId)
        throws ResourceNotFoundException, UnauthorizedException;


    Page<LatestReviewResponseDto> getLatestReviews(Pageable pageable);
    List<LatestReviewResponseDto> getLatestReviews(int count);
}