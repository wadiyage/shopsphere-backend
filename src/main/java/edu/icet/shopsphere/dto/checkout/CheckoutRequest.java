package edu.icet.shopsphere.dto.checkout;

import edu.icet.shopsphere.entity.enums.PaymentMethod;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutRequest {
    private String shippingAddress;
    private PaymentMethod paymentMethod;
}
