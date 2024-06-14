package com.test.technicalapi.resource;

import lombok.Data;

@Data
public class ProductOrderResource {
    private Long id;
    private String name;
    private double unitPrice;
    int quantity;
    double totalPrice;
}
