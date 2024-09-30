package com.kagimbz.inventoryservice;

import com.kagimbz.inventoryservice.model.InventoryItem;
import com.kagimbz.inventoryservice.repository.InventoryRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner loadData(InventoryRepo inventoryRepo) {
//		return args -> {
//			InventoryItem inventoryItem = new InventoryItem();
//			inventoryItem.setSkuCode("USB-C charger");
//			inventoryItem.setQuantity(20);
//
//			InventoryItem inventoryItem2 = new InventoryItem();
//			inventoryItem2.setSkuCode("Oraimo Earphones");
//			inventoryItem2.setQuantity(0);
//
//			inventoryRepo.saveAll(List.of(inventoryItem, inventoryItem2));
//		};
//	}

}
