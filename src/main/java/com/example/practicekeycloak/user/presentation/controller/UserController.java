package com.example.practicekeycloak.user.presentation.controller;

import com.example.practicekeycloak.user.application.dto.TokenInfo;
import com.example.practicekeycloak.user.application.service.TokenGenerateService;
import com.example.practicekeycloak.user.presentation.dto.TokenRequest;
import com.example.practicekeycloak.user.presentation.dto.TokenResponse;
import com.example.practicekeycloak.user.presentation.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name="회원 API", description = "")
public class UserController {

    private final TokenGenerateService tokenService;

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

    @Operation(summary = "로그인한 사용자 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
    @Parameter(name="Authorization", description = "인증 방식 및 토큰을 위한 헤더", example = "Bearer 인증토큰", in = ParameterIn.HEADER, schema = @Schema(format = "string"))
    @GetMapping()
    public UserResponse getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        Map<String, Object> claims = jwt.getClaims();

        return new UserResponse(userId, (String) claims.getOrDefault("preferred_username", ""));
    }
}
