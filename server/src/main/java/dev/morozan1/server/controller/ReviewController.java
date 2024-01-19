package dev.morozan1.server.controller;

import dev.morozan1.server.dto.CreateReviewRequestDto;
import dev.morozan1.server.dto.ReviewResponseDto;
import dev.morozan1.server.dto.UpdateReviewRequestDto;
import dev.morozan1.server.entity.Machine;
import dev.morozan1.server.entity.Review;
import dev.morozan1.server.exception.BadRequestException;
import dev.morozan1.server.exception.BadIdException;
import dev.morozan1.server.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import dev.morozan1.server.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    private final ModelMapper modelMapper;

    @Autowired
    public ReviewController(ReviewService reviewService, ModelMapper modelMapper) {
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getReviews() {
        List<Review> reviews = reviewService.getReviews();
        List<ReviewResponseDto> reviewResponseDtoList = reviews.stream()
                .map(review -> modelMapper.map(review, ReviewResponseDto.class))
                .toList();
        return new ResponseEntity<>(reviewResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/machines/{machineId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByMachine(@PathVariable String machineId) {
        try {
            if (machineId == null) throw new BadIdException();
            long machineIdValue = Long.parseLong(machineId);

            List<Review> reviews = reviewService.getReviewsByMachineId(machineIdValue);
            List<ReviewResponseDto> reviewResponseDtoList = reviews.stream()
                    .map(review -> modelMapper.map(review, ReviewResponseDto.class))
                    .toList();

            return new ResponseEntity<>(reviewResponseDtoList, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponseDto> getReview(@PathVariable String id) {
        if (id == null) throw new BadIdException();
        try {
            Long idValue = Long.parseLong(id);
            Review review = reviewService.getReview(idValue);
            ReviewResponseDto reviewResponseDto = modelMapper.map(review, ReviewResponseDto.class);
            return new ResponseEntity<>(reviewResponseDto, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }

    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@Validated @RequestBody CreateReviewRequestDto createReviewRequestDto) {
        Review review = modelMapper.map(createReviewRequestDto, Review.class);
        Review createdReview = reviewService.createReview(review);
        ReviewResponseDto reviewResponseDto = modelMapper.map(createdReview, ReviewResponseDto.class);
        return new ResponseEntity<>(reviewResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable String id, @Validated @RequestBody UpdateReviewRequestDto updateReviewRequestDto) {
        if (id == null) throw new BadIdException();
        try {
            Long idValue = Long.parseLong(id);
            Review review = modelMapper.map(updateReviewRequestDto, Review.class);
            Review updatedReview = reviewService.updateReview(idValue, review);
            ReviewResponseDto reviewResponseDto = modelMapper.map(updatedReview, ReviewResponseDto.class);
            return new ResponseEntity<>(reviewResponseDto, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        if (id == null) throw new BadIdException();
        try {
            Long idValue = Long.parseLong(id);
            reviewService.deleteReview(idValue);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadIdException();
        }
    }
}
