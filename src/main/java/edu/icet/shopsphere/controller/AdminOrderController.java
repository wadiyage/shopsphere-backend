package edu.icet.shopsphere.controller;

import edu.icet.shopsphere.dto.order.OrderResponse;
import edu.icet.shopsphere.dto.order.UpdateOrderStatusRequest;
import edu.icet.shopsphere.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return adminOrderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return adminOrderService.getOrderById(id);
    }

    @PutMapping("/{id}/status")
    public OrderResponse updateOrderStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequest request) {
        return adminOrderService.updateOrderStatus(id, request);
    }
}
