package dev.morozan1.server.dto.request;

import dev.morozan1.server.annotation.IsBoolean;
import dev.morozan1.server.annotation.IsDouble;
import lombok.Data;

@Data
public class CUMachineProductRequestDto {

        @IsBoolean(message = "isAvailable must be true or false")
        private String isAvailable;

        @IsDouble(message = "Price must be a double")
        private String price;
}
