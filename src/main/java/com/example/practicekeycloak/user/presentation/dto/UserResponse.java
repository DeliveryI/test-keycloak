package com.example.practicekeycloak.user.presentation.dto;

import java.util.UUID;

public record UserResponse(
        UUID userId,
        String username
        // TODO: 필드 추가 필요
) {}
