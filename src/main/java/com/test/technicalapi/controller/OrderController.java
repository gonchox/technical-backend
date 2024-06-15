package com.test.technicalapi.controller;

import com.test.technicalapi.domain.model.Order;
import com.test.technicalapi.domain.model.Product;
import com.test.technicalapi.domain.service.OrderService;
import com.test.technicalapi.domain.service.ProductService;
import com.test.technicalapi.resource.OrderResource;
import com.test.technicalapi.resource.ProductResource;
import com.test.technicalapi.resource.SaveOrderResource;
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

@Tag(name = "orders", description = "Orders API")
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OrderController {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Get Orders", description = "Get All Orders", tags = { "orders" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Orders returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/orders")
    public List<OrderResource> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return orders.stream().map(this::convertToResource).collect(Collectors.toList());
    }

    @Operation( summary = "Get all orders by product id")
    @GetMapping("/products/{productId}/orders")
    public List<OrderResource> getAllOrdersByProductId(@PathVariable(name = "productId") Long productId) {
        List<Order> orders = orderService.getAllOrdersByProductId(productId);
        return orders.stream().map(this::convertToResource).collect(Collectors.toList());
    }

    @Operation(summary = "Get Order by Id", description = "Get a Order by specifying Id", tags = { "orders" })
    @GetMapping("/orders/{id}")
    public OrderResource getOrderById(
            @Parameter(description="Order Id")
            @PathVariable(name = "id") Long orderId) {
        return convertToResource(orderService.getOrderById(orderId));
    }

    @PostMapping("/orders")
    public OrderResource createOrder(@Valid @RequestBody SaveOrderResource resource)  {
        Order order = convertToEntity(resource);
        return convertToResource(orderService.createOrder(order));
    }

    @PutMapping("/orders/{id}")
    public OrderResource updateOrder(@PathVariable(name = "id") Long orderId, @Valid @RequestBody SaveOrderResource resource) {
        Order order = convertToEntity(resource);
        return convertToResource(orderService.updateOrder(orderId, order));
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable(name = "id") Long orderId) {
        return orderService.deleteOrder(orderId);
    }

    @Operation(summary = "Assign a product to an order")
    @PostMapping("/orders/{orderId}/products/{productId}/{quantity}")
    public OrderResource assignOrderProduct(@PathVariable(name = "orderId") Long orderId,
                                            @PathVariable(name = "productId") Long productId,
                                            @PathVariable(name = "quantity") int quantity){
        return convertToResource(orderService.addProduct(orderId, productId,quantity));
    }

    @Operation(summary = "Deassign a product to an order")
    @DeleteMapping("/orders/{orderId}/products/{productId}/{quantity}")
    public OrderResource unassignOrderProduct(@PathVariable(name = "orderId") Long orderId,
                                              @PathVariable(name = "productId") Long productId,
                                              @PathVariable(name = "quantity") int quantity) {

        return convertToResource(orderService.deleteProduct(orderId, productId, quantity));
    }

    @Operation(summary = "Update quantity of a product in an order")
    @PutMapping("/orders/{orderId}/products/{productId}/{quantity}")
    public OrderResource updateOrderProductQuantity(
            @PathVariable(name = "orderId") Long orderId,
            @PathVariable(name = "productId") Long productId,
            @PathVariable(name = "quantity") int quantity) {

        return convertToResource(orderService.updateProductQuantity(orderId, productId, quantity));
    }

    // Auto Mapper
    private Order convertToEntity(SaveOrderResource resource) {
        return mapper.map(resource, Order.class);
    }

    private OrderResource convertToResource(Order entity) {
        return mapper.map(entity, OrderResource.class);
    }
}
