package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.basic.OrderItem;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.basic.Review;
import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.create.ReviewCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.ReviewResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ReviewUpdateDto;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.extra.mappers.ReviewMapper;
import com.uade.tpo.marketplace.repository.interfaces.IOrderItemRepository;
import com.uade.tpo.marketplace.repository.interfaces.IProductRepository;
import com.uade.tpo.marketplace.repository.interfaces.IReviewRepository;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepository;
import com.uade.tpo.marketplace.service.interfaces.IReviewService;

import jakarta.transaction.Transactional;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    private IReviewRepository reviewRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IOrderItemRepository orderItemRepository;

    private final ReviewMapper reviewMapper;

    public ReviewService() {
        this.reviewMapper = new ReviewMapper();
    }






    @Override
    @Transactional(rollbackOn = Throwable.class)
    public ReviewResponseDto createReview(ReviewCreateDto dto, Long buyerId) throws ResourceNotFoundException, BadRequestException {
        if (dto == null) throw new BadRequestException("Datos de la reseña no proporcionados.");
        if (dto.productId() == null) throw new BadRequestException("Debe indicarse el productId para la reseña.");
        if (dto.rating() == null || dto.rating() < 1 || dto.rating() > 10) {
            throw new BadRequestException("La calificación (rating) debe estar entre 1 y 10.");
        }

        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado (id=" + dto.productId() + ")."));

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario comprador no encontrado (id=" + buyerId + ")."));




        if (dto.orderItemId() != null) {
            OrderItem oi = orderItemRepository.findById(dto.orderItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("OrderItem no encontrado (id=" + dto.orderItemId() + ")."));

            if (oi.getOrder() == null || oi.getOrder().getBuyer() == null || !Objects.equals(oi.getOrder().getBuyer().getId(), buyerId)) {
                throw new BadRequestException("El OrderItem no pertenece al comprador indicado.");
            }
            if (oi.getProduct() == null || !Objects.equals(oi.getProduct().getId(), dto.productId())) {
                throw new BadRequestException("El OrderItem no corresponde al producto indicado.");
            }
            if (oi.getReview() != null) {
                throw new BadRequestException("Ya existe una reseña asociada a ese OrderItem.");
            }
        } else {

        }


        Review review = reviewMapper.toEntity(dto);
        review.setBuyer(buyer);
        review.setProduct(product);
        review.setSeller(product.getSeller()); 
        review.setVisible(true);


        Review saved = reviewRepository.save(review);


        if (dto.orderItemId() != null) {
            OrderItem oi = orderItemRepository.findById(dto.orderItemId()).get();
            oi.setReview(saved);

            orderItemRepository.save(oi);
        }

        return reviewMapper.toResponse(saved);
    }


    @Override
    @Transactional(rollbackOn = Throwable.class)
    public ReviewResponseDto updateReview(Long reviewId, ReviewUpdateDto dto, Long requestingUserId) throws ResourceNotFoundException, UnauthorizedException {
        if (reviewId == null) throw new ResourceNotFoundException("Id de reseña no proporcionado.");
        Review existing = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada (id=" + reviewId + ")."));

        User requester = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario solicitante no encontrado (id=" + requestingUserId + ")."));

        boolean isOwner = existing.getBuyer() != null && Objects.equals(existing.getBuyer().getId(), requestingUserId);

        if (!isOwner) {
            throw new UnauthorizedException("No tiene permiso para modificar esta reseña.");
        }

        reviewMapper.updateFromDto(dto, existing);

        Review saved = reviewRepository.save(existing);
        return reviewMapper.toResponse(saved);
    }

    @Override
    @Transactional(rollbackOn = Throwable.class)
    public void deleteReview(Long reviewId, Long requestingUserId) throws ResourceNotFoundException, UnauthorizedException {
        if (reviewId == null) throw new ResourceNotFoundException("Id de reseña no proporcionado.");
        Review existing = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada (id=" + reviewId + ")."));

        User requester = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario solicitante no encontrado (id=" + requestingUserId + ")."));

        boolean isOwner = existing.getBuyer() != null && Objects.equals(existing.getBuyer().getId(), requestingUserId);
        if (!isOwner) {
            throw new UnauthorizedException("No tiene permiso para eliminar esta reseña.");
        }

        if (existing.getOrderItem() != null) {
            OrderItem oi = orderItemRepository.findById(existing.getOrderItem().getId()).orElse(null);
            if (oi != null) {
                oi.setReview(null);
                orderItemRepository.save(oi);
            }
        }

        reviewRepository.delete(existing);
    }


    @Override
    public Page<ReviewResponseDto> getReviewsByProduct(Long productId, Pageable pageable, boolean onlyVisible) throws ResourceNotFoundException {
        if (productId == null) throw new ResourceNotFoundException("productId no proporcionado.");
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado (id=" + productId + ")."));

        Page<Review> page = reviewRepository.findByProductId(productId, pageable);
        List<ReviewResponseDto> dtos = page.getContent().stream()
                .filter(r -> !onlyVisible || r.isVisible())
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }


    @Override
    public Page<ReviewResponseDto> getReviewsByUser(Long userId, Pageable pageable) throws ResourceNotFoundException {
        if (userId == null) throw new ResourceNotFoundException("userId no proporcionado.");
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado (id=" + userId + ")."));

        Page<Review> page = reviewRepository.findByBuyerId(userId, pageable);
        List<ReviewResponseDto> dtos = page.getContent().stream().map(reviewMapper::toResponse).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public Page<ReviewResponseDto> getAllReviews(Pageable pageable) {
        Page<Review> page = reviewRepository.findAll(pageable);
        List<ReviewResponseDto> dtos = page.getContent().stream().map(reviewMapper::toResponse).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }


    @Override
    public Pair<Double, Long> getAverageRatingAndCountByProduct(Long productId) throws ResourceNotFoundException {
        if (productId == null) throw new ResourceNotFoundException("productId no proporcionado.");
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado (id=" + productId + ")."));

        Pageable p = Pageable.ofSize(1000);
        Page<Review> page = reviewRepository.findByProductId(productId, p);
        List<Review> visible = page.getContent().stream().filter(Review::isVisible).collect(Collectors.toList());

        long count = visible.size();
        double avg = 0.0;
        if (count > 0) {
            double sum = visible.stream()
                    .filter(r -> r.getRating() != null)
                    .mapToDouble(Review::getRating)
                    .sum();
            avg = sum / count;
        }

        return Pair.of(avg, count);
    }
}
