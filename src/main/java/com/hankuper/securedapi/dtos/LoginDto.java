package com.hankuper.securedapi.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDto {

    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
}
