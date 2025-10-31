package com.example.practicekeycloak.user.application.service;

import com.example.practicekeycloak.user.application.dto.TokenInfo;

public interface TokenGenerateService {
    TokenInfo generate(String username, String password);
}
