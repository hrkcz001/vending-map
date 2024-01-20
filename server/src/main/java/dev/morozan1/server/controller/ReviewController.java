package dev.morozan1.server.controller;

import dev.morozan1.server.dto.CUReviewRequestDto;
import dev.morozan1.server.dto.ReviewResponseDto;
import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.entity.Review;
import dev.morozan1.server.exception.BadIdException;
import dev.morozan1.server.service.MachineService;
import dev.morozan1.server.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machines")
public class ReviewController {

    private final MachineService machineService;
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewController(MachineService machineService, ReviewService reviewService, ModelMapper modelMapper) {
        this.machineService = machineService;
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByMachine(@PathVariable String id) {
        if (id == null) throw new BadIdException();
        try {
            long idValue = Long.parseLong(id);

            Machine machine = machineService.getMachine(idValue);
            List<ReviewResponseDto> reviewResponseDtoList = machine.getReviews().stream()
                    .map(review -> modelMapper.map(review, ReviewResponseDto.class))
                    .toList();

            return new ResponseEntity<>(reviewResponseDtoList, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }

    @GetMapping("/{machineId}/reviews/{reviewId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewByMachine(@PathVariable String machineId, @PathVariable String reviewId) {
        if (machineId == null || reviewId == null) throw new BadIdException();
        try {
            long machineIdValue = Long.parseLong(machineId);
            long reviewIdValue = Long.parseLong(reviewId);

            Review review = reviewService.getReviewByReviewIdAndMachineId(reviewIdValue, machineIdValue);
            ReviewResponseDto reviewResponseDto = modelMapper.map(review, ReviewResponseDto.class);

            return new ResponseEntity<>(List.of(reviewResponseDto), HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@PathVariable String id, @Validated @RequestBody CUReviewRequestDto reviewRequestDto) {
        if (id == null) throw new BadIdException();
        try {
            long idValue = Long.parseLong(id);

            Machine machine = machineService.getMachine(idValue);
            Review review = modelMapper.map(reviewRequestDto, Review.class);
            Review createdReview = reviewService.createReview(review);
            machine.addReview(review);

            ReviewResponseDto createdReviewResponseDto = modelMapper.map(createdReview, ReviewResponseDto.class);

            return new ResponseEntity<>(createdReviewResponseDto, HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }

    @PutMapping("/{machineId}/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable String machineId, @PathVariable String reviewId, @Validated @RequestBody CUReviewRequestDto reviewRequestDto) {
        if (machineId == null || reviewId == null) throw new BadIdException();
        try {
            long machineIdValue = Long.parseLong(machineId);
            long reviewIdValue = Long.parseLong(reviewId);

            Review newReview = modelMapper.map(reviewRequestDto, Review.class);
            Review updatedReview = reviewService.updateReview(machineIdValue, reviewIdValue, newReview);
            ReviewResponseDto updatedReviewResponseDto = modelMapper.map(updatedReview, ReviewResponseDto.class);

            return new ResponseEntity<>(updatedReviewResponseDto, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }

    @DeleteMapping("/{machineId}/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String machineId, @PathVariable String reviewId) {
        if (machineId == null || reviewId == null) throw new BadIdException();
        try {
            long machineIdValue = Long.parseLong(machineId);
            long reviewIdValue = Long.parseLong(reviewId);

            reviewService.deleteReview(machineIdValue, reviewIdValue);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }
}
