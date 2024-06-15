package com.test.technicalapi.domain.repository;

import com.test.technicalapi.domain.model.Order;
import com.test.technicalapi.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface OrderRepository {
    List<Order> getAllOrdersByProductId(Long orderId);

    ResponseEntity<?> deleteOrder(Long orderId);

    Order updateOrder(Long orderId, Order orderRequest);

    Order createOrder(Order order);

    Order addProduct(Long orderId, Long productId, int quantity);

    Order deleteProduct(Long orderId, Long productId, int quantity);

    List<Order> getAllOrders();

    Order getOrderById(Long orderId);

    Order updateOrderProduct(Long orderId, Long productId, int quantity);
}
