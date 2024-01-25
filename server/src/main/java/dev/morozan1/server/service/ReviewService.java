package dev.morozan1.server.service;

import dev.morozan1.server.entity.Review;
import dev.morozan1.server.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review getReviewByReviewIdAndMachineId(long reviewId, long machineId) {
        return reviewRepository.findByMachineAndReviewIds(machineId, reviewId).orElseThrow();
    }

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review updateReview(long machineId, long reviewId, Review review) {
        Objects.requireNonNull(review);
        Review reviewToUpdate = reviewRepository.findByMachineAndReviewIds(machineId, reviewId).orElseThrow();
        reviewToUpdate.setRating(review.getRating());
        reviewToUpdate.setComment(review.getComment());
        return reviewRepository.save(reviewToUpdate);
    }

    public void deleteReview(long machineId, long reviewId) {
        Review review = reviewRepository.findByMachineAndReviewIds(machineId, reviewId).orElseThrow();
        reviewRepository.delete(review);
    }
}
