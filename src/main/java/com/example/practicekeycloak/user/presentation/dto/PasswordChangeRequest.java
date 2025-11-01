package com.example.practicekeycloak.user.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordChangeRequest(
   @NotBlank String currentPassword,

   @NotBlank @NotBlank @Size(min=8, max=15) String newPassword,

   @NotBlank String confirmPassword
) {}
