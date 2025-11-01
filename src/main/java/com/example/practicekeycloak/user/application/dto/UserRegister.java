package com.example.practicekeycloak.user.application.dto;

import lombok.Builder;

@Builder
public record UserRegister (
        String username,
        String password,
        String nickname,
        String userPhone,
        String currentAddress
) {}
