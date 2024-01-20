package dev.morozan1.server.dto.response;

import lombok.Data;

@Data
public class ReviewResponseDto {
    private String id;
    private String machineId;
    private String rating;
    private String comment;
}
