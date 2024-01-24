package dev.morozan1.server.dto.response;

import lombok.Data;

@Data
public class MachineProductResponseDto {

    private ProductResponseDto product;
    private Boolean isAvailable;
    private int price;
}