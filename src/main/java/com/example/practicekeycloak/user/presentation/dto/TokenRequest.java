package com.example.practicekeycloak.user.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank
        String username,
        @NotBlank
        String password
) {}
