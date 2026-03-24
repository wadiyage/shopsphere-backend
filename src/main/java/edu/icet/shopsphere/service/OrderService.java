package edu.icet.shopsphere.service;

import edu.icet.shopsphere.dto.order.OrderRequest;
import edu.icet.shopsphere.dto.order.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    List<OrderResponse> getMyOrders();
    OrderResponse getOrderById(Long id);
//    void cancelOrder(Long id);
}
