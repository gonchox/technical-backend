package com.test.technicalapi.service;

import com.test.technicalapi.domain.model.Order;
import com.test.technicalapi.domain.model.Product;
import com.test.technicalapi.domain.repository.OrderRepository;
import com.test.technicalapi.domain.repository.ProductRepository;
import com.test.technicalapi.domain.service.ProductService;
import com.test.technicalapi.exception.ResourceNotFoundException;
import com.test.technicalapi.resource.ProductOrderResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductOrderResource> getAllProductsByOrderId(Long orderId) {
        return productRepository.getAllProductsByOrderId(orderId);
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long productId) {
        return productRepository.deleteProduct(productId);
    }

    @Override
    public Product updateProduct(Long productId, Product productRequest) {
        return productRepository.updateProduct(productId, productRequest);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.createProduct(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public Product findProductById(Long productId) {
        return productRepository.findProductById(productId);
    }
}
