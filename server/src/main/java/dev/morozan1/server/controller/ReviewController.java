package dev.morozan1.server.controller;

import dev.morozan1.server.dto.request.CUReviewRequestDto;
import dev.morozan1.server.dto.response.ReviewResponseDto;
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
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByMachine(@PathVariable long id) {
        Machine machine = machineService.getMachine(id);
        List<ReviewResponseDto> reviewResponseDtoList = machine.getReviews().stream()
                .map(review -> modelMapper.map(review, ReviewResponseDto.class))
                .toList();
        return new ResponseEntity<>(reviewResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/{machineId}/reviews/{reviewId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewByMachine(@PathVariable long machineId,
                                                                      @PathVariable long reviewId) {
        Review review = reviewService.getReviewByReviewIdAndMachineId(reviewId, machineId);
        ReviewResponseDto reviewResponseDto = modelMapper.map(review, ReviewResponseDto.class);

        return new ResponseEntity<>(List.of(reviewResponseDto), HttpStatus.OK);
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@PathVariable long id,
                                                          @Validated @RequestBody CUReviewRequestDto reviewRequestDto) {
        Machine machine = machineService.getMachine(id);
        Review review = modelMapper.map(reviewRequestDto, Review.class);
        Review createdReview = reviewService.createReview(review);
        machine.addReview(review);

        ReviewResponseDto createdReviewResponseDto = modelMapper.map(createdReview, ReviewResponseDto.class);

        return new ResponseEntity<>(createdReviewResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{machineId}/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable long machineId,
                                                          @PathVariable long reviewId,
                                                          @Validated @RequestBody CUReviewRequestDto reviewRequestDto) {
        Review newReview = modelMapper.map(reviewRequestDto, Review.class);
        Review updatedReview = reviewService.updateReview(machineId, reviewId, newReview);
        ReviewResponseDto updatedReviewResponseDto = modelMapper.map(updatedReview, ReviewResponseDto.class);

        return new ResponseEntity<>(updatedReviewResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{machineId}/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable long machineId,
                                             @PathVariable long reviewId) {
            reviewService.deleteReview(machineId, reviewId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
