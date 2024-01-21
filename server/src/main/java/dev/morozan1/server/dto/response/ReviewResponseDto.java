package dev.morozan1.server.dto.response;

import lombok.Data;

@Data
public class ReviewResponseDto {
    private Long reviewId;
    private Long machineId;
    private Short rating;
    private String comment;
}
