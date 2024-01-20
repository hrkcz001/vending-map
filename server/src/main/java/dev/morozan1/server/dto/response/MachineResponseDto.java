package dev.morozan1.server.dto.response;

import dev.morozan1.server.dto.CoordinatesDto;
import dev.morozan1.server.dto.TimePeriodDto;
import lombok.Data;

import java.util.List;

@Data
public class MachineResponseDto {

    private Long machineId;
    private CoordinatesDto coordinates;
    private String address;
    private String description;

    private Double rating;
    private Integer reviewsCount;

    private TimePeriodDto availableTime;
}
