package dev.morozan1.server.dto;

import lombok.Data;

@Data
public class MachineProductResponseDto {

    private ProductResponseDto product;
    private Boolean isAvailable;
    private Double price;
}