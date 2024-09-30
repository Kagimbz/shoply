package com.kagimbz.inventoryservice.repository;

import com.kagimbz.inventoryservice.model.InventoryItem;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Observed
public interface InventoryRepo extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findBySkuCodeIn(List<String> skuCodes);
}
