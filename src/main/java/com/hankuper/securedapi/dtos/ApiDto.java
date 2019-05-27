package com.hankuper.securedapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiDto {
    private Boolean success;
    private String message;
}
