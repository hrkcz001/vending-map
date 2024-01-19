package dev.morozan1.server.dto;

import dev.morozan1.server.annotation.IsMachineId;
import dev.morozan1.server.annotation.IsRating;
import dev.morozan1.server.annotation.NullOrNotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateReviewRequestDto {

    @IsMachineId
    private String machineId;

    @NotNull
    @IsRating
    private String rating;

    @NullOrNotBlank(message = "Comment should be null or not blank")
    private String comment;
}
