package com.test.technicalapi.service;

import com.test.technicalapi.domain.model.Order;
import com.test.technicalapi.domain.model.Product;
import com.test.technicalapi.domain.repository.OrderRepository;
import com.test.technicalapi.domain.repository.ProductRepository;
import com.test.technicalapi.domain.service.OrderService;
import com.test.technicalapi.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> getAllOrdersByProductId(Long productId) {
        return orderRepository.getAllOrdersByProductId(productId);
    }

    @Override
    public ResponseEntity<?> deleteOrder(Long orderId) {
        return orderRepository.deleteOrder(orderId);
    }

    @Override
    public Order updateOrder(Long orderId, Order orderRequest) {
        return orderRepository.updateOrder(orderId, orderRequest);
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.createOrder(order);
    }

    @Override
    public Order addProduct(Long orderId, Long productId, int quantity) {
        return orderRepository.addProduct(orderId, productId, quantity);
    }

    @Override
    public Order deleteProduct(Long orderId, Long productId, int quantity) {
        return orderRepository.deleteProduct(orderId, productId,quantity);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.getOrderById(orderId);
    }

    @Override
    public Order updateProductQuantity(Long orderId, Long productId, int quantity) {
        return orderRepository.updateOrderProduct(orderId, productId, quantity);
    }
}
