package dev.morozan1.server.dto;

import dev.morozan1.server.annotation.IsRating;
import dev.morozan1.server.annotation.NullOrNotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateReviewRequestDto {

    @NotNull
    @IsRating
    private String rating;

    @NullOrNotBlank(message = "Comment should be null or not blank")
    private String comment;
}
