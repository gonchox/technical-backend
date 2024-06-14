package com.test.technicalapi.resource;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.test.technicalapi.domain.model.Order;
import lombok.Data;

import java.util.List;

@Data
public class SaveProductResource {
    private String name;
    private double unitPrice;
}
