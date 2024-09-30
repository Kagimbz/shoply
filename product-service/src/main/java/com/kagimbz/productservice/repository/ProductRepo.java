package com.kagimbz.productservice.repository;

import com.kagimbz.productservice.model.Product;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Observed
public interface ProductRepo extends MongoRepository<Product, String> {
}
