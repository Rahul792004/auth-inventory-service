package com.inventory_system;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemResponse {
    private Long id;
    private String name;
    private int quantity;
}
