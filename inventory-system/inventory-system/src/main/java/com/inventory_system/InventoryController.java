package com.inventory_system;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/items")
    public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody ItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.createItem(request));
    }

    @GetMapping("/items")
    public ResponseEntity<Page<ItemResponse>> getAllItems(Pageable pageable) {
        return ResponseEntity.ok(inventoryService.getAllItems(pageable));
    }

    @PostMapping("/items/{id}/stock-in")
    public ResponseEntity<ItemResponse> stockIn(@PathVariable Long id,
                                                 @Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(inventoryService.stockIn(id, request));
    }

    @PostMapping("/items/{id}/stock-out")
    public ResponseEntity<ItemResponse> stockOut(@PathVariable Long id,
                                                  @Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(inventoryService.stockOut(id, request));
    }
}
