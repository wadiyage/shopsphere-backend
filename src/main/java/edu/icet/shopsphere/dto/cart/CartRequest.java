package edu.icet.shopsphere.dto.cart;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartRequest {
    private Long productId;
    private Integer quantity;
}
