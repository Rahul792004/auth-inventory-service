package com.inventory_system;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ItemRepository itemRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    @Transactional
    public ItemResponse createItem(ItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setQuantity(request.getQuantity());
        Item saved = itemRepository.save(item);
        return toResponse(saved);
    }

    public Page<ItemResponse> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional
    public ItemResponse stockIn(Long id, StockRequest request) {
        Item item = findItem(id);
        item.setQuantity(item.getQuantity() + request.getQuantity());
        Item saved = itemRepository.save(item);
        saveTransaction(saved, TransactionType.STOCK_IN, request.getQuantity());
        return toResponse(saved);
    }

    @Transactional
    public ItemResponse stockOut(Long id, StockRequest request) {
        Item item = findItem(id);
        if (item.getQuantity() < request.getQuantity()) {
            throw new BadRequestException("Insufficient stock. Available: " + item.getQuantity());
        }
        item.setQuantity(item.getQuantity() - request.getQuantity());
        Item saved = itemRepository.save(item);
        saveTransaction(saved, TransactionType.STOCK_OUT, request.getQuantity());
        return toResponse(saved);
    }

    private Item findItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
    }

    private void saveTransaction(Item item, TransactionType type, int quantity) {
        TransactionHistory tx = new TransactionHistory();
        tx.setItem(item);
        tx.setType(type);
        tx.setQuantity(quantity);
        transactionHistoryRepository.save(tx);
    }

    private ItemResponse toResponse(Item item) {
        return new ItemResponse(item.getId(), item.getName(), item.getQuantity());
    }
}
