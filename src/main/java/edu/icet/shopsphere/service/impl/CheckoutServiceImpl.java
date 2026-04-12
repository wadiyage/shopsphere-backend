package edu.icet.shopsphere.service.impl;

import edu.icet.shopsphere.dto.address.AddressResponse;
import edu.icet.shopsphere.dto.checkout.CheckoutRequest;
import edu.icet.shopsphere.dto.order.OrderItemResponse;
import edu.icet.shopsphere.dto.order.OrderResponse;
import edu.icet.shopsphere.entity.*;
import edu.icet.shopsphere.entity.enums.OrderStatus;
import edu.icet.shopsphere.exception.ResourceNotFoundException;
import edu.icet.shopsphere.repository.AddressRepository;
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
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public OrderResponse checkout(CheckoutRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + request.getAddressId()));

        if(!address.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Address not found with id: " + request.getAddressId());
        }

        List<CartItem> cartItems = cartItemRepository.findAllByUser(user);

        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        } else {
            double totalAmount = cartItems.stream()
                    .mapToDouble(ci -> {
                        if (ci.getProduct().getStockQuantity() < ci.getQuantity()) {
                            throw new ResourceNotFoundException("Product " + ci.getProduct().getName() + " is out of stock");
                        } else {
                            ci.getProduct().setStockQuantity(
                                    ci.getProduct().getStockQuantity() - ci.getQuantity()
                            );
                            return ci.getProduct().getPrice() * ci.getQuantity();
                        }
                    }).sum();

            Order order = Order.builder()
                    .user(user)
                    .address(address)
                    .totalAmount(totalAmount)
                    .status(OrderStatus.PENDING)
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

    public void handlePaymentSuccess(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        for(OrderItem item: order.getOrderItems()) {
            Product product = item.getProduct();

            if(product.getStockQuantity() < item.getQuantity()) {
                throw new ResourceNotFoundException("Product " + product.getName() + " is out of stock");
            }

            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
        }

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
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
                .address(AddressResponse.builder()
                        .id(order.getAddress().getId())
                        .fullName(order.getAddress().getFullName())
                        .phone(order.getAddress().getPhone())
                        .addressLine(order.getAddress().getAddressLine())
                        .city(order.getAddress().getCity())
                        .state(order.getAddress().getState())
                        .postalCode(order.getAddress().getPostalCode())
                        .build()
                )
                .totalAmount((order.getTotalAmount()))
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(orderItemResponses)
                .build();
    }
}
