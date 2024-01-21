package dev.morozan1.server.dto.request;

import dev.morozan1.server.annotation.NullOrNotBlank;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CUReviewRequestDto {

    @NotNull
    @DecimalMin(value = "0", message = "Rating should be decimal number between 1 and 5")
    @DecimalMax(value = "5", message = "Rating should be decimal number between 1 and 5")
    private Short rating;

    @NullOrNotBlank(message = "Comment should be null or not blank")
    private String comment;
}
