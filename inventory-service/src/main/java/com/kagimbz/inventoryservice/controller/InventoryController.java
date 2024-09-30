package com.kagimbz.inventoryservice.controller;

import com.kagimbz.inventoryservice.dto.InventoryItemRequest;
import com.kagimbz.inventoryservice.dto.InventoryResponse;
import com.kagimbz.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/inventory")
@Slf4j
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping(path = "/add-item")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String addItem(@RequestBody InventoryItemRequest inventoryItemRequest) {
        inventoryService.addItem(inventoryItemRequest);
        return "Item Added Successfully";
    }

    @GetMapping(path = "/check-item")
    @ResponseStatus(value = HttpStatus.OK)
    public List<InventoryResponse> checkItem(@RequestParam("skuCodes") List<String> skuCodes) {
        return inventoryService.checkItem(skuCodes);
    }
}
