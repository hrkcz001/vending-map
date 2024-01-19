package dev.morozan1.server.dto;

import lombok.Data;

@Data
public class ProductResponseDto {

        private Long productId;
        private String name;
        private String imageUrl;
        private Double averagePrice;
}
