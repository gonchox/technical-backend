package com.test.technicalapi.resource;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.test.technicalapi.domain.model.Order;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.List;

@Data
public class ProductResource {
    private Long id;
    private String name;
    private double unitPrice;
}
