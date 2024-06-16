package com.test.technicalapi.domain.repository.Impl;

import com.test.technicalapi.domain.model.Order;
import com.test.technicalapi.domain.model.Product;
import com.test.technicalapi.domain.repository.OrderRepository;
import com.test.technicalapi.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Order> getAllOrdersByProductId(Long productId) {
        String sql = "SELECT o.* FROM orders o " +
                "JOIN order_product op ON o.id = op.order_id " +
                "WHERE op.product_id = ?";
        return jdbcTemplate.query(sql, new Object[]{productId}, new BeanPropertyRowMapper<>(Order.class));
    }

    @Override
    public ResponseEntity<?> deleteOrder(Long orderId) {
        String deleteOrderSql = "DELETE FROM orders WHERE id = ?";
        int deletedCount = jdbcTemplate.update(deleteOrderSql, orderId);
        if (deletedCount > 0) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public Order updateOrder(Long orderId, Order orderRequest) {
        String updateOrderSql = "UPDATE orders SET orderNumber = ? WHERE id = ?";
        jdbcTemplate.update(updateOrderSql, orderRequest.getOrderNumber(), orderId);
        return getOrderById(orderId);
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        String insertOrderSql = "INSERT INTO orders (orderNumber, finalPrice, date) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertOrderSql, order.getOrderNumber(), order.getFinalPrice(), order.getDate());

        Long orderId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        order.setId(orderId);

        return order;
    }

    @Override
    @Transactional
    public Order addProduct(Long orderId, Long productId, int quantity) {
        // Check if the product already exists in the order
        String checkExistenceSql = "SELECT quantity FROM order_product WHERE order_id = ? AND product_id = ?";
        Integer existingQuantity = jdbcTemplate.query(checkExistenceSql, new Object[]{orderId, productId},
                (rs, rowNum) -> rs.getInt("quantity")).stream().findFirst().orElse(null);

        if (existingQuantity != null) {
            // If product exists, update the quantity
            String updateQuantitySql = "UPDATE order_product SET quantity = ? WHERE order_id = ? AND product_id = ?";
            jdbcTemplate.update(updateQuantitySql, existingQuantity + quantity, orderId, productId);
        } else {
            // Otherwise, add a new entry
            String insertProductSql = "INSERT INTO order_product (order_id, product_id, quantity) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertProductSql, orderId, productId, quantity);
        }

        updateOrderFinalPrice(orderId);
        updateNumProducts(orderId);
        return getOrderById(orderId);
    }

    private void updateNumProducts(Long orderId) {
        // Calculate numProducts
        String countProductsSql = "SELECT COALESCE(SUM(quantity), 0) AS numProducts FROM order_product WHERE order_id = ?";
        Integer numProducts = jdbcTemplate.queryForObject(countProductsSql, Integer.class, orderId);

        // Update numProducts in the orders table
        String updateNumProductsSql = "UPDATE orders SET numProducts = ? WHERE id = ?";
        jdbcTemplate.update(updateNumProductsSql, numProducts, orderId);
    }

    @Override
    @Transactional
    public Order updateOrderProduct(Long orderId, Long productId, int quantity) {
        // Check if the product exists in the order
        String checkExistenceSql = "SELECT quantity FROM order_product WHERE order_id = ? AND product_id = ?";
        Integer currentQuantity = jdbcTemplate.query(checkExistenceSql, new Object[]{orderId, productId},
                (rs, rowNum) -> rs.getInt("quantity")).stream().findFirst().orElse(null);

        if (currentQuantity != null) {
            // Update the quantity of the product in the order
            String updateQuantitySql = "UPDATE order_product SET quantity = ? WHERE order_id = ? AND product_id = ?";
            jdbcTemplate.update(updateQuantitySql, quantity, orderId, productId);

            updateOrderFinalPrice(orderId);
            updateNumProducts(orderId);
        } else {
            throw new ResourceNotFoundException("Product not found with id " + productId + " in order " + orderId);
        }

        return getOrderById(orderId);
    }

    @Override
    @Transactional
    public Order deleteProduct(Long orderId, Long productId, int quantity) {
        // Check if the product exists in the order
        String checkExistenceSql = "SELECT quantity FROM order_product WHERE order_id = ? AND product_id = ?";
        Integer currentQuantity = jdbcTemplate.query(checkExistenceSql, new Object[]{orderId, productId},
                (rs, rowNum) -> rs.getInt("quantity")).stream().findFirst().orElse(null);

        if (currentQuantity != null) {
            int newQuantity = currentQuantity - quantity;
            if (newQuantity > 0) {
                // Update quantity if new quantity is greater than zero
                String updateQuantitySql = "UPDATE order_product SET quantity = ? WHERE order_id = ? AND product_id = ?";
                jdbcTemplate.update(updateQuantitySql, newQuantity, orderId, productId);
            } else {
                // Remove the product from the order if new quantity is zero or less
                String deleteProductSql = "DELETE FROM order_product WHERE order_id = ? AND product_id = ?";
                jdbcTemplate.update(deleteProductSql, orderId, productId);
            }

            updateOrderFinalPrice(orderId);
            updateNumProducts(orderId);
        }

        return getOrderById(orderId);
    }


    private void updateOrderFinalPrice(Long orderId) {
        String sql = "SELECT SUM(p.unitPrice * op.quantity) FROM products p " +
                "JOIN order_product op ON p.id = op.product_id " +
                "WHERE op.order_id = ?";
        Double totalSum = jdbcTemplate.queryForObject(sql, Double.class, orderId);

        if (totalSum == null) {
            totalSum = 0.0; // Handle cases where no products are associated with the order
        }

        String updateSql = "UPDATE orders SET finalPrice = ? WHERE id = ?";
        jdbcTemplate.update(updateSql, totalSum, orderId);
    }

    @Override
    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class));
    }

    @Override
    public Order getOrderById(Long orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{orderId}, new BeanPropertyRowMapper<>(Order.class));
    }

    // Helper method to insert products for an order
    private void insertProductsForOrder(Long orderId, List<Product> products) {
        for (Product product : products) {
            String insertProductSql = "INSERT INTO order_product (order_id, product_id) VALUES (?, ?)";
            jdbcTemplate.update(insertProductSql, orderId, product.getId());
        }
    }
}
