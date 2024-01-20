package dev.morozan1.server.dto;

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
