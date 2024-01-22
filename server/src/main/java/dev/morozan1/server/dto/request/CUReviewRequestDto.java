package dev.morozan1.server.dto.request;

import dev.morozan1.server.annotation.NullOrNotBlank;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CUReviewRequestDto {

    @NotNull
    @DecimalMin(value = "0", message = "Rating should be decimal number between 1 and 5")
    @DecimalMax(value = "5", message = "Rating should be decimal number between 1 and 5")
    private Short rating;

    @NotBlank(message = "Comment should not be blank")
    @Size(max = 255, message = "Comment should be less than 300 characters")
    private String comment;
}
