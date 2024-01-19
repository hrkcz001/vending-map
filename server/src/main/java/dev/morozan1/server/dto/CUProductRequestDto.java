package dev.morozan1.server.dto;

import dev.morozan1.server.annotation.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CUProductRequestDto {

        @NotBlank(message = "Product name must not be blank")
        private String name;

        @NullOrNotBlank(message = "Product image url should be null or not blank")
        private String picture;
}
