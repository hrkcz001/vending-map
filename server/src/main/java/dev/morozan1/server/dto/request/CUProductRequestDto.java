package dev.morozan1.server.dto.request;

import dev.morozan1.server.annotation.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CUProductRequestDto {

        @NotBlank(message = "Product name must not be blank")
        @Size(max = 50, message = "Product name should be less than 50 characters")
        private String name;

        @NullOrNotBlank(message = "Product image url should be null or not blank")
        @Size(max = 255, message = "Product image url should be less than 255 characters")
        private String picture;
}
