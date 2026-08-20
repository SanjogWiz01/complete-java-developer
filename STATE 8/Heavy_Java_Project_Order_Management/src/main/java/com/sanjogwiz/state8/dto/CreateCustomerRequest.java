package com.sanjogwiz.state8.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest(
        @NotBlank(message = "Full name is required")
        String fullName,
        @Email(message = "Email must be valid")
        @NotBlank(message = "Email is required")
        String email
) {
}

