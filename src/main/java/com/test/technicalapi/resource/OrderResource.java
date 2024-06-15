package com.test.technicalapi.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.test.technicalapi.domain.model.AuditModel;
import com.test.technicalapi.domain.model.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class OrderResource extends AuditModel {

    private Long id;
    private String orderNumber;
    private double finalPrice;
    private int numProducts;
    @JsonManagedReference
    @JsonIgnore
    private List<Product> products;
}
