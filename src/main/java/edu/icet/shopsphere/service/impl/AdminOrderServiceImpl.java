package edu.icet.shopsphere.service.impl;

import edu.icet.shopsphere.dto.order.OrderItemResponse;
import edu.icet.shopsphere.dto.order.OrderResponse;
import edu.icet.shopsphere.dto.order.UpdateOrderStatusRequest;
import edu.icet.shopsphere.entity.Order;
import edu.icet.shopsphere.entity.enums.OrderStatus;
import edu.icet.shopsphere.exception.InvalidOrderStatusException;
import edu.icet.shopsphere.exception.ResourceNotFoundException;
import edu.icet.shopsphere.repository.OrderRepository;
import edu.icet.shopsphere.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return orders.stream().map(this::mapToResponse).toList();
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(order.getOrderItems().stream().map(oi -> OrderItemResponse.builder()
                        .productId(oi.getProduct().getId())
                        .productName(oi.getProduct().getName())
                        .quantity(oi.getQuantity())
                        .price(oi.getPrice())
                        .build()).toList())
                .build();
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToResponse(order);
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if(!isValidTransition(order.getStatus(), request.getOrderStatus())) {
            throw new InvalidOrderStatusException("Delivered order cannot be updated");
        } else {
            order.setStatus(request.getOrderStatus());
            Order updated = orderRepository.save(order);
            return mapToResponse(updated);
            }
    }

    private boolean isValidTransition(OrderStatus current, OrderStatus next) {
        return switch (current) {
            case PENDING -> next.equals(OrderStatus.PAID);
            case PAID -> next.equals(OrderStatus.PROCESSING) || next.equals(OrderStatus.CANCELLED);
            case PROCESSING -> next.equals(OrderStatus.SHIPPED);
            case SHIPPED -> next.equals(OrderStatus.DELIVERED);
            default -> false;
        };
    }
}
