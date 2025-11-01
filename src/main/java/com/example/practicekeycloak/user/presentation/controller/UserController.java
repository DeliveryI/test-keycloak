package com.example.practicekeycloak.user.presentation.controller;

import com.example.practicekeycloak.user.application.dto.TokenInfo;
import com.example.practicekeycloak.user.application.dto.UserRegister;
import com.example.practicekeycloak.user.application.service.TokenGenerateService;
import com.example.practicekeycloak.user.application.service.UserRegisterService;
import com.example.practicekeycloak.user.application.service.UserUpdateService;
import com.example.practicekeycloak.user.presentation.dto.*;
import com.example.practicekeycloak.user.presentation.validator.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name="회원 API", description = "")
public class UserController {

    private final TokenGenerateService tokenService;
    private final UserRegisterService userRegisterService;
    private final UserUpdateService userUpdateService;

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

    @Operation(summary = "회원가입", description = "신규 사용자를 등록합니다. 가입 시 기본 권한은 'CUSTOMER' 입니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("signup")
    public void signUp(@Valid @RequestBody UserRegisterRequest req) {
        new UserValidator().validate(req);

        UserRegister dto = UserRegister.builder()
                .username(req.username())
                .password(req.password())
                .nickname(req.nickname())
                .userPhone(req.userPhone())
                .currentAddress(req.currentAddress())
                .build();

        userRegisterService.register(dto);
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
    @PatchMapping("password")
    public void changePassword(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody PasswordChangeRequest req) {
        // 비밀번호가 일치하는지 검증
        TokenInfo tokenInfo = tokenService.generate(jwt.getSubject(), req.currentPassword());
        if (tokenInfo == null) {
            //
        }

        // 비밀번호 validation
        new UserValidator().validate(req);

        userUpdateService.updatePassword(UUID.fromString(jwt.getSubject()), req.newPassword());
    }

    @Operation(summary = "역할 변경", description = "회원의 역할을 변경합니다. 'MASTER' 회원만 접근할 수 있습니다.")
    @PatchMapping("role")
    public void changeRole(@AuthenticationPrincipal Jwt jwt, @RequestBody List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            // throw new BadRequestException("변경할 ROLE을 전송해 주세요.");
        }

        userUpdateService.updateUserRole(UUID.fromString(jwt.getSubject()), roles);
    }
}
