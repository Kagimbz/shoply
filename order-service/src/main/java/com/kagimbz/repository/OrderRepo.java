package com.kagimbz.repository;

import com.kagimbz.model.Order;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Observed
public interface OrderRepo extends JpaRepository<Order, Long> {
}
