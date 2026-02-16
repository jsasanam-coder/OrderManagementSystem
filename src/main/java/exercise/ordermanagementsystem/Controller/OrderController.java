package exercise.ordermanagementsystem.Controller;

import exercise.ordermanagementsystem.Dto.OrderRequestDTO;
import exercise.ordermanagementsystem.Dto.OrderResponseDTO;
import exercise.ordermanagementsystem.Entities.Order;
import exercise.ordermanagementsystem.Service.OrderService;
import exercise.ordermanagementsystem.model.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Order Management", description = "APIs for managing orders")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create order", description = "Creates a new order with stock validation and total calculation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "404", description = "Customer or product not found"),
            @ApiResponse(responseCode = "400", description = "Insufficient stock or validation failed")
    })
    @PostMapping("/")
    public ResponseEntity<Order> createOrder(@RequestBody @Valid OrderRequestDTO orderRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderRequestDTO));
    }

    @Operation(summary = "Update order status", description = "Updates the status of an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid status or id supplied")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable @NotNull @PositiveOrZero Integer id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @Operation(summary = "Get order with details", description = "Returns full order details including customer and all order items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderWithDetails(
            @NotNull @PositiveOrZero @PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrderWithDetails(id));
    }

    @Operation(summary = "Get customer orders", description = "Returns all orders placed by a specific customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "400", description = "Invalid customer id supplied")
    })
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getCustomerOrders(
            @NotNull @PositiveOrZero @PathVariable Integer customerId) {
        return ResponseEntity.ok(orderService.getCustomerOrders(customerId));
    }
}
