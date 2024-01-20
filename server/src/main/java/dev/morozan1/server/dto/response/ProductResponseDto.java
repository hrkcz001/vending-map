package dev.morozan1.server.dto.response;

import lombok.Data;

@Data
public class ProductResponseDto {

        private Long productId;
        private String name;
        private String picture;
        private Double averagePrice;
}
