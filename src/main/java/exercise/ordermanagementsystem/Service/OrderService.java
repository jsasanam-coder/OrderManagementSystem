package exercise.ordermanagementsystem.Service;

import exercise.ordermanagementsystem.Dao.CustomerRepository;
import exercise.ordermanagementsystem.Dao.OrderItemRepository;
import exercise.ordermanagementsystem.Dao.OrderRepository;
import exercise.ordermanagementsystem.Dao.ProductRepository;
import exercise.ordermanagementsystem.Dto.OrderItemRequestDTO;
import exercise.ordermanagementsystem.Dto.OrderItemResponseDTO;
import exercise.ordermanagementsystem.Dto.OrderRequestDTO;
import exercise.ordermanagementsystem.Dto.OrderResponseDTO;
import exercise.ordermanagementsystem.Entities.Customer;
import exercise.ordermanagementsystem.Entities.Order;
import exercise.ordermanagementsystem.Entities.OrderItem;
import exercise.ordermanagementsystem.Entities.Product;
import exercise.ordermanagementsystem.Exception.InsufficientStockException;
import exercise.ordermanagementsystem.Exception.ResourceNotFoundException;
import exercise.ordermanagementsystem.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public Order createOrder(OrderRequestDTO orderRequestDTO) {
        Customer customer = customerRepository.findById(orderRequestDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Order newOrder = Order.builder()
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalAmount(0.0)
                .build();
        orderRepository.save(newOrder);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemRequestDTO item : orderRequestDTO.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            if (product.getStock() < item.getQuantity()) {
                throw new InsufficientStockException("Not enough stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            OrderItem newOrderItem = OrderItem.builder()
                    .order(newOrder)
                    .product(product)
                    .quantity(item.getQuantity())
                    .price(product.getPrice())
                    .build();
            orderItems.add(newOrderItem);
            totalAmount += product.getPrice() * item.getQuantity();
        }

        orderItemRepository.saveAll(orderItems);
        newOrder.setTotalAmount(totalAmount);
        return orderRepository.save(newOrder);
    }

    public Order updateOrderStatus(Integer id, OrderStatus status) {
        Order updatedOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        updatedOrder.setStatus(status);
        return orderRepository.save(updatedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderWithDetails(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        List<OrderItem> orderItems = orderItemRepository.findByOrder_Id(orderId);
        List<OrderItemResponseDTO> orderItemResponseDTOS = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemResponseDTO dto = OrderItemResponseDTO.builder()
                    .id(orderItem.getId())
                    .productName(orderItem.getProduct().getName())
                    .quantity(orderItem.getQuantity())
                    .price(orderItem.getPrice())
                    .build();
            orderItemResponseDTOS.add(dto);
        }

        return OrderResponseDTO.builder()
                .id(order.getId())
                .customerName(order.getCustomer().getName())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .items(orderItemResponseDTOS)
                .build();
    }

    @Transactional(readOnly = true)
    public List<Order> getCustomerOrders(Integer customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        return orderRepository.findCustomerOrdersWithDetails(customerId);
    }
}
