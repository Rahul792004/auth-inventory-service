package com.inventory_system;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ItemRequest {

    @NotBlank(message = "Name must not be empty")
    private String name;

    @Min(value = 0, message = "Quantity must be >= 0")
    private int quantity;
}
