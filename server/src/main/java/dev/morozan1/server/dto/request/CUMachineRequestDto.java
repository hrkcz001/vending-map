package dev.morozan1.server.dto.request;

import dev.morozan1.server.annotation.IsOptionalTimePeriod;
import dev.morozan1.server.annotation.NullOrNotBlank;
import dev.morozan1.server.annotation.PlaceInArea;
import dev.morozan1.server.dto.CoordinatesDto;
import dev.morozan1.server.dto.TimePeriodDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CUMachineRequestDto {

        @NotBlank(message = "Address can't be blank")
        @Size(max = 50, message = "Address should be less than 50 characters")
        private String address;

        @NullOrNotBlank(message = "Description can't be whitespace-only")
        @Size(max = 255, message = "Description should be less than 300 characters")
        private String description;

        @PlaceInArea
        private CoordinatesDto coordinates;

        @IsOptionalTimePeriod
        private TimePeriodDto availableTime;
}