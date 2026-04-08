package edu.icet.shopsphere.service.impl;

import edu.icet.shopsphere.dto.checkout.CheckoutRequest;
import edu.icet.shopsphere.dto.order.OrderItemResponse;
import edu.icet.shopsphere.dto.order.OrderResponse;
import edu.icet.shopsphere.entity.CartItem;
import edu.icet.shopsphere.entity.Order;
import edu.icet.shopsphere.entity.OrderItem;
import edu.icet.shopsphere.entity.User;
import edu.icet.shopsphere.entity.enums.OrderStatus;
import edu.icet.shopsphere.exception.ResourceNotFoundException;
import edu.icet.shopsphere.repository.CartItemRepository;
import edu.icet.shopsphere.repository.OrderRepository;
import edu.icet.shopsphere.service.CheckoutService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponse checkout(CheckoutRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        List<CartItem> cartItems = cartItemRepository.findAllByUser(user);

        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        } else {
            double totalAmount = cartItems.stream()
                    .mapToDouble(ci -> {
                        if (ci.getProduct().getStockQuantity() < ci.getQuantity()) {
                            throw new ResourceNotFoundException("Product " + ci.getProduct().getName() + " is out of stock");
                        } else {
                            ci.getProduct().setStockQuantity(ci.getProduct().getStockQuantity() - ci.getQuantity());
                            return ci.getProduct().getPrice() * ci.getQuantity();
                        }
                    }).sum();

            Order order = Order.builder()
                    .user(user)
                    .totalAmount(totalAmount)
                    .status(OrderStatus.PENDING)
                    .shippingAddress(request.getShippingAddress())
                    .paymentMethod(request.getPaymentMethod())
                    .build();


            List<OrderItem> orderItems = cartItems.stream()
                    .map(ci -> OrderItem.builder()
                            .order(order)
                            .product(ci.getProduct())
                            .quantity(ci.getQuantity())
                            .price(ci.getProduct().getPrice())
                            .build())
                    .toList();

            order.setOrderItems(orderItems);
            Order savedOrder = orderRepository.save(order);
            cartItemRepository.deleteAll(cartItems);
            return mapToOrderResponse(savedOrder);
        }
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map(oi -> OrderItemResponse.builder()
                        .productId(oi.getProduct().getId())
                        .productName(oi.getProduct().getName())
                        .quantity(oi.getQuantity())
                        .price(oi.getPrice())
                        .build()
                ).toList();

        return OrderResponse.builder()
                .id(order.getId())
                .totalAmount((order.getTotalAmount()))
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(orderItemResponses)
                .shippingAddress(order.getShippingAddress())
                .paymentMethod(order.getPaymentMethod())
                .build();
    }
}
