package com.test.technicalapi.domain.service;

import com.test.technicalapi.domain.model.Order;
import com.test.technicalapi.domain.model.Product;
import com.test.technicalapi.resource.ProductOrderResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductOrderResource> getAllProductsByOrderId(Long orderId);

    ResponseEntity<?> deleteProduct(Long productId);

    Product updateProduct(Long productId, Product productRequest);

    Product createProduct(Product product);

    List<Product> getAllProducts();

    Product findProductById(Long productId);
}
