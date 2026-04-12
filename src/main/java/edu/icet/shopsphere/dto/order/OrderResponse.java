package edu.icet.shopsphere.dto.order;

import edu.icet.shopsphere.dto.address.AddressResponse;
import edu.icet.shopsphere.entity.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private AddressResponse address;
    private Double totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}
