package edu.icet.shopsphere.service;

import edu.icet.shopsphere.dto.cart.CartRequest;
import edu.icet.shopsphere.dto.cart.CartResponse;

import java.util.List;

public interface CartService {
    CartResponse addToCart(CartRequest request);
    List<CartResponse> getCartItems();
    CartResponse updateCartItem(Long id, CartRequest request);
    void removeFromCart(Long id);
    void clearCart();
}
