package com.test.technicalapi.domain.repository.Impl;

import com.test.technicalapi.domain.model.Product;
import com.test.technicalapi.domain.repository.ProductRepository;
import com.test.technicalapi.resource.ProductOrderResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Product findProductById(Long productId) {
        String sql = "SELECT * FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, new BeanPropertyRowMapper<>(Product.class));
    }

    @Override
    public List<ProductOrderResource> getAllProductsByOrderId(Long orderId) {
        String sql = "SELECT p.*, op.quantity, (p.unitPrice * op.quantity) AS totalPrice " +
                "FROM products p " +
                "JOIN order_product op ON p.id = op.product_id " +
                "WHERE op.order_id = ?";
        return jdbcTemplate.query(sql, new Object[]{orderId}, (rs, rowNum) -> {
            ProductOrderResource productResource = new ProductOrderResource();
            productResource.setId(rs.getLong("id"));
            productResource.setName(rs.getString("name"));
            productResource.setUnitPrice(rs.getDouble("unitPrice"));
            productResource.setQuantity(rs.getInt("quantity"));
            productResource.setTotalPrice(rs.getDouble("totalPrice"));
            return productResource;
        });
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long productId) {
        String sql = "DELETE FROM products WHERE id = ?";
        int deletedCount = jdbcTemplate.update(sql, productId);
        if (deletedCount > 0) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public Product updateProduct(Long productId, Product productRequest) {
        String sql = "UPDATE products SET name = ?, unitPrice = ? WHERE id = ?";
        jdbcTemplate.update(sql, productRequest.getName(), productRequest.getUnitPrice(), productId);
        return findProductById(productId); // Return the updated product
    }

    @Override
    public Product createProduct(Product product) {
        String sql = "INSERT INTO products (name, unitPrice) VALUES (?, ?)";
        jdbcTemplate.update(sql, product.getName(), product.getUnitPrice());

        Long productId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return findProductById(productId);
    }

    @Override
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class));
    }


}
