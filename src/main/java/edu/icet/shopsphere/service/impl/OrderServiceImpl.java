package edu.icet.shopsphere.service.impl;

import edu.icet.shopsphere.dto.order.OrderItemResponse;
import edu.icet.shopsphere.dto.order.OrderResponse;
import edu.icet.shopsphere.entity.Order;
import edu.icet.shopsphere.entity.User;
import edu.icet.shopsphere.exception.ResourceNotFoundException;
import edu.icet.shopsphere.exception.UnauthorizedException;
import edu.icet.shopsphere.repository.CartItemRepository;
import edu.icet.shopsphere.repository.OrderRepository;
import edu.icet.shopsphere.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public List<OrderResponse> getMyOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        List<Order> orders = orderRepository.findAllByUser(user);
        return orders.stream().map(this::mapToResponse).collect(Collectors.toList());
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if(!order.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Unauthorized to view this order");
        } else {
            return mapToResponse(order);
        }
    }

//    @Override
//    public void cancelOrder(Long id) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) auth.getPrincipal();
//
//        Order order = orderRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
//
//        if(!order.getUser().getId().equals(user.getId())) {
//            throw new UnauthorizedException("Unauthorized to cancel this order");
//        } else {
//            order.setStatus(OrderStatus.CANCELLED);
//            orderRepository.save(order);
//        }
//    }
}
