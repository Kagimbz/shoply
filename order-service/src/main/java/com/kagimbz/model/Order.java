package com.kagimbz.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ToString.Exclude
    private List<OrderLineItem> orderLineItems;



    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        for (OrderLineItem item : orderLineItems) {
            item.setOrder(this);
        }

        this.orderLineItems = orderLineItems;
    }



}
