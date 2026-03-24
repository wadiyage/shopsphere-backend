package edu.icet.shopsphere.service.impl;

import edu.icet.shopsphere.dto.order.OrderItemResponse;
import edu.icet.shopsphere.dto.order.OrderRequest;
import edu.icet.shopsphere.dto.order.OrderResponse;
import edu.icet.shopsphere.entity.CartItem;
import edu.icet.shopsphere.entity.Order;
import edu.icet.shopsphere.entity.OrderItem;
import edu.icet.shopsphere.entity.User;
import edu.icet.shopsphere.entity.enums.OrderStatus;
import edu.icet.shopsphere.exception.ResourceNotFoundException;
import edu.icet.shopsphere.exception.UnauthorizedException;
import edu.icet.shopsphere.repository.CartItemRepository;
import edu.icet.shopsphere.repository.OrderRepository;
import edu.icet.shopsphere.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        if (request.getCartItemIds() == null || request.getCartItemIds().isEmpty()) {
            throw new RuntimeException("Cart item ids cannot be null or empty");
        } else {
            List<CartItem> cartItems = request.getCartItemIds().stream()
                    .map(id -> cartItemRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + id)))
                    .peek(ci -> {
                        if (!ci.getUser().getId().equals(user.getId())) {
                            throw new UnauthorizedException("Cart item with id: " + ci.getId() + " does not belong to the user");
                        }
                    })
                    .collect(Collectors.toList());


            Double totalAmount = cartItems.stream()
                    .mapToDouble(ci -> ci.getProduct().getPrice() * ci.getQuantity())
                    .sum();

            Order order = Order.builder()
                    .user(user)
                    .totalAmount(totalAmount)
                    .status(OrderStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();

            List<OrderItem> orderItems = cartItems.stream()
                    .map(ci -> OrderItem.builder()
                            .order(order)
                            .product(ci.getProduct())
                            .quantity(ci.getQuantity())
                            .price(ci.getProduct().getPrice())
                            .build())
                    .collect(Collectors.toList());

            order.setOrderItems(orderItems);
            Order savedOrder = orderRepository.save(order);

            // Clear the cart items after placing the order
            cartItemRepository.deleteAll(cartItems);
            return mapToResponse(savedOrder);
        }
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
    public List<OrderResponse> getMyOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        List<Order> orders = orderRepository.findAllByUser(user);
        return orders.stream().map(this::mapToResponse).collect(Collectors.toList());
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
