package edu.icet.shopsphere.controller;

import edu.icet.shopsphere.dto.cart.CartRequest;
import edu.icet.shopsphere.dto.cart.CartResponse;
import edu.icet.shopsphere.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartResponse addToCart(@RequestBody CartRequest request) {
        return cartService.addToCart(request);
    }

    @GetMapping
    public List<CartResponse> getCartItems() {
        return cartService.getCartItems();
    }

    @PutMapping("/{id}")
    public CartResponse updateCartItem(@PathVariable Long id, @RequestBody CartRequest request) {
        return cartService.updateCartItem(id, request);
    }

    @DeleteMapping("/{id}")
    public void removeCartItem(@PathVariable Long id) {
        cartService.removeFromCart(id);
    }

    @DeleteMapping
    public void clearCart() {
        cartService.clearCart();
    }
}
