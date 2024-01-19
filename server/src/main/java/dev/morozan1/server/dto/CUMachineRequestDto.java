package dev.morozan1.server.dto;

import dev.morozan1.server.annotation.IsOptionalTimePeriod;
import dev.morozan1.server.annotation.NullOrNotBlank;
import dev.morozan1.server.annotation.PlaceInArea;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CUMachineRequestDto {

        @NotBlank(message = "Address can't be blank")
        private String address;

        @NullOrNotBlank(message = "Description can't be whitespace-only")
        private String description;

        @PlaceInArea
        private CoordinatesDto coordinates;

        @IsOptionalTimePeriod
        private TimePeriodDto availableTime;
}