package com.example.practicekeycloak.user.presentation.controller;

import com.example.practicekeycloak.user.application.dto.TokenInfo;
import com.example.practicekeycloak.user.application.service.TokenGenerateService;
import com.example.practicekeycloak.user.presentation.dto.TokenRequest;
import com.example.practicekeycloak.user.presentation.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name="회원 API", description = "")
public class UserController {

    private final TokenGenerateService tokenService;

    // 토큰 발급
    // TODO : return ResponseEntity<ApiResponse<T>>
    @Operation(summary = "인증 토큰 발급", description = "username, password 인증을 통해서 승인된 회원이 접근할 수 있는 토큰을 발급합니다.")
    @PostMapping("login")
    public TokenResponse generateToken(@Valid @RequestBody TokenRequest req) {
        TokenInfo tokenInfo = tokenService.generate(req.username(), req.password());

        return new TokenResponse(
                tokenInfo.access_token(),
                tokenInfo.expires_in(),
                tokenInfo.refresh_expires_in(),
                tokenInfo.refresh_token(),
                tokenInfo.token_type());
    }

}
