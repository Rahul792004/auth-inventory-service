package com.inventory_system;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class StockRequest {

    @Min(value = 1, message = "Quantity must be > 0")
    private int quantity;
}
