package com.test.technicalapi.resource;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.test.technicalapi.domain.model.AuditModel;
import com.test.technicalapi.domain.model.Product;
import lombok.Data;

import java.util.List;

@Data
public class SaveOrderResource extends AuditModel {

    private String orderNumber;
}
