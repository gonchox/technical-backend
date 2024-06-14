package com.test.technicalapi.domain.repository;

import com.test.technicalapi.domain.model.Order;
import com.test.technicalapi.domain.model.Product;
import com.test.technicalapi.resource.ProductOrderResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    public Product findProductById(Long productId);

    public List<ProductOrderResource> getAllProductsByOrderId(Long orderId);

    public ResponseEntity<?> deleteProduct(Long productId);

    public Product updateProduct(Long productId, Product productRequest);

    public Product createProduct(Product product);

    public List<Product> getAllProducts();

}
