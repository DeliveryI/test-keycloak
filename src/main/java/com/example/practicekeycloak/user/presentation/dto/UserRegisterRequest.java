package com.example.practicekeycloak.user.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest (
        @NotBlank @Size(min=4, max=10) String username,
        @NotBlank @Size(min=8, max=15) String password,
        @NotBlank String confirmPassword,
        @NotBlank @Size(max=20) String nickname,
        @NotBlank String userPhone,
        String currentAddress
) {}
