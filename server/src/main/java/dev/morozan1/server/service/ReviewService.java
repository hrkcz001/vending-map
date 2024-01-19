package dev.morozan1.server.service;

import dev.morozan1.server.entity.Review;
import dev.morozan1.server.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByMachineId(long machineId) {
        return reviewRepository.findAllByMachineId(machineId);
    }

    public Review getReview(Long id) {
        return reviewRepository.findById(id).orElseThrow();
    }

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review updateReview(Long id, Review review) {
        Review reviewToUpdate = reviewRepository.findById(id).orElseThrow();
        reviewToUpdate.setRating(review.getRating());
        reviewToUpdate.setComment(review.getComment());
        return reviewRepository.save(reviewToUpdate);
    }

    public void deleteReview(Long id) {
        reviewRepository.findById(id).orElseThrow();
        reviewRepository.deleteById(id);
    }
}
