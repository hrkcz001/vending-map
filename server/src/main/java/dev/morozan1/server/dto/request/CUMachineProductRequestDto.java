package dev.morozan1.server.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class CUMachineProductRequestDto {

        private Boolean isAvailable;

        @DecimalMin(value = "1", message = "Price should be greater than 0")
        @DecimalMax(value = "1000", message = "Price should be less than or equal 1000")
        private int price;
}
