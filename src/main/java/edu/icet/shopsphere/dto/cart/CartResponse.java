package edu.icet.shopsphere.dto.cart;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    private Long id;

    private Long productId;
    private String productName;
    private String productDescription;

    private Double productPrice;
    private String imageUrl;

    private String categoryName;

    private Integer quantity;
    private Double totalPrice;

    private Integer stockQuantity;
    private Boolean inStock;
}
