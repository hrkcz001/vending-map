package dev.morozan1.server.service;

import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.entity.Review;
import dev.morozan1.server.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private final long MACHINE_ID = 1;
    private final long REVIEW_ID = 2;

    @Test
    public void testGetReviewByReviewIdAndMachineId() {
        Review mockReview = new Review();
        Mockito.when(reviewRepository.findByMachineAndReviewIds(MACHINE_ID, REVIEW_ID)).thenReturn(Optional.of(mockReview));

        Review result = reviewService.getReviewByReviewIdAndMachineId(REVIEW_ID, MACHINE_ID);

        assertNotNull(result);
    }

    @Test
    public void testCreateReview() {
        Review reviewToCreate = new Review();
        Mockito.when(reviewRepository.save(reviewToCreate)).thenReturn(reviewToCreate);

        Review createdReview = reviewService.createReview(reviewToCreate);

        assertEquals(reviewToCreate, createdReview);
    }

    @Test
    public void testUpdateReview() {
        Machine machine = new Machine();
        machine.setMachineId(MACHINE_ID);

        Review oldReview = new Review();
        oldReview.setMachine(machine);
        oldReview.setRating((short) 4);
        oldReview.setComment("Old Comment");

        Review newReview = new Review();
        newReview.setMachine(machine);
        newReview.setRating((short) 5);
        newReview.setComment("New Comment");

        when(reviewRepository.findByMachineAndReviewIds(MACHINE_ID, REVIEW_ID)).thenReturn(Optional.of(oldReview));
        when(reviewRepository.save(oldReview)).thenReturn(oldReview);

        Review updatedReview = reviewService.updateReview(MACHINE_ID, REVIEW_ID, newReview);

        assertEquals(newReview.getRating(), updatedReview.getRating());
        assertEquals(newReview.getComment(), updatedReview.getComment());
    }

    @Test
    public void testDeleteReview() {
        Machine machine = new Machine();
        machine.setMachineId(MACHINE_ID);

        Review existingReview = new Review();
        existingReview.setMachine(machine);

        when(reviewRepository.findByMachineAndReviewIds(MACHINE_ID, REVIEW_ID)).thenReturn(Optional.of(existingReview));

        reviewService.deleteReview(MACHINE_ID, REVIEW_ID);

        Mockito.verify(reviewRepository, Mockito.times(1)).delete(existingReview);
    }
}