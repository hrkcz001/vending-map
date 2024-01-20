package dev.morozan1.server.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponseDto {

    private Integer status;
    private String error;
    private List<String> reasons;
}
