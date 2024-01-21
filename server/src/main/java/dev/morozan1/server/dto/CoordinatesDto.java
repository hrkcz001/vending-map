package dev.morozan1.server.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CoordinatesDto {

    @NotNull(message = "Latitude can't be null")
    private Double latitude;

    @NotNull(message = "Longitude can't be null")
    private Double longitude;
}