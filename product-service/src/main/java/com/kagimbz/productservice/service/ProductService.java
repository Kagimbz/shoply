package com.kagimbz.productservice.service;

import com.kagimbz.productservice.dto.AddProductRequest;
import com.kagimbz.productservice.dto.ProductResponse;
import com.kagimbz.productservice.model.Product;
import com.kagimbz.productservice.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);


    public void addProduct(@RequestBody AddProductRequest addProductRequest) {
        Product product = Product.builder()
                .name(addProductRequest.getName())
                .description(addProductRequest.getDescription())
                .price(addProductRequest.getPrice())
                .build();

        productRepo.insert(product);

        LOGGER.info("Product with id {} saved successfully", product.getId());

    }

    public List<ProductResponse> getAllProducts() {

        return productRepo.findAll()
                .stream()
                .map(ProductService::mapToResponse)
                .collect(Collectors.toList());

    }

    private static ProductResponse mapToResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
