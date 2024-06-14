package com.test.technicalapi.controller;

import com.test.technicalapi.domain.model.Order;
import com.test.technicalapi.domain.model.Product;
import com.test.technicalapi.domain.service.ProductService;
import com.test.technicalapi.resource.OrderResource;
import com.test.technicalapi.resource.ProductOrderResource;
import com.test.technicalapi.resource.ProductResource;
import com.test.technicalapi.resource.SaveProductResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "products", description = "Products API")
@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProductService productService;

    @Operation(summary = "Get Products", description = "Get All Products", tags = { "products" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Products returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/products")
    public List<ProductResource> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return products.stream().map(this::convertToResource).collect(Collectors.toList());
    }

    @Operation( summary = "Get all products by order id")
    @GetMapping("/orders/{orderId}/products")
    public List<ProductOrderResource> getAllProductsByOrderId(@PathVariable(name = "orderId") Long orderId) {
        List<ProductOrderResource> products = productService.getAllProductsByOrderId(orderId);
        return products;
    }

    @Operation(summary = "Get Product by Id", description = "Get a Product by specifying Id", tags = { "products" })
    @GetMapping("/products/{id}")
    public ProductResource getProductById(
            @Parameter(description="Product Id")
            @PathVariable(name = "id") Long productId) {
        return convertToResource(productService.findProductById(productId));
    }

    @PostMapping("/products")
    public ProductResource createProduct(@Valid @RequestBody SaveProductResource resource)  {
        Product product = convertToEntity(resource);
        return convertToResource(productService.createProduct(product));
    }

    @PutMapping("/products/{id}")
    public ProductResource updateProduct(@PathVariable(name = "id") Long productId, @Valid @RequestBody SaveProductResource resource) {
        Product product = convertToEntity(resource);
        return convertToResource(productService.updateProduct(productId, product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(name = "id") Long productId) {
        return productService.deleteProduct(productId);
    }

    // Auto Mapper
    private Product convertToEntity(SaveProductResource resource) {
        return mapper.map(resource, Product.class);
    }

    private ProductResource convertToResource(Product entity) {
        return mapper.map(entity, ProductResource.class);
    }

    private ProductOrderResource convertToResource2(Product entity) {
        return mapper.map(entity, ProductOrderResource.class);
    }
}
