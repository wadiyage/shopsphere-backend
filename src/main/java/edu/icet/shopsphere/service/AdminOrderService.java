package edu.icet.shopsphere.service;

import edu.icet.shopsphere.dto.order.OrderResponse;
import edu.icet.shopsphere.dto.order.UpdateOrderStatusRequest;

import java.util.List;

public interface AdminOrderService {
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(Long id);
    OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request);
}
