package com.inventory_system;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Password must be min 8 chars with uppercase, number, and special character"
    )
    private String password;

    @NotNull(message = "Role must be ADMIN or STAFF")
    private Role role;
}
