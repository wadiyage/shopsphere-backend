package edu.icet.shopsphere.dto.order;

import edu.icet.shopsphere.entity.enums.OrderStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderStatusRequest {
    private OrderStatus orderStatus;
}
