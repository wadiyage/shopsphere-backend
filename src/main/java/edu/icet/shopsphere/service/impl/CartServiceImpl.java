package edu.icet.shopsphere.service.impl;

import edu.icet.shopsphere.dto.cart.CartRequest;
import edu.icet.shopsphere.dto.cart.CartResponse;
import edu.icet.shopsphere.entity.CartItem;
import edu.icet.shopsphere.entity.Product;
import edu.icet.shopsphere.entity.User;
import edu.icet.shopsphere.repository.CartItemRepository;
import edu.icet.shopsphere.repository.ProductRepository;
import edu.icet.shopsphere.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    public CartResponse addToCart(CartRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));

        CartItem exiting = cartItemRepository.findByUserAndProduct(user, product);
        if(exiting != null) {
            exiting.setQuantity(exiting.getQuantity() + request.getQuantity());
            CartItem updated = cartItemRepository.save(exiting);
            return mapToResponse(updated);
        } else {
            CartItem cartItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();

            CartItem saved = cartItemRepository.save(cartItem);
            return mapToResponse(saved);
        }
    }

    private CartResponse mapToResponse(CartItem cartItem) {
        return CartResponse.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .productPrice(cartItem.getProduct().getPrice())
                .quantity(cartItem.getQuantity())
                .totalPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .build();
    }

    @Override
    public List<CartResponse> getCartItems() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        List<CartItem> cartItems = cartItemRepository.findAllByUser(user);
        return cartItems.stream().map(this::mapToResponse).toList();
    }

    @Override
    public CartResponse updateCartItem(Long id, CartRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + id));

        if(!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this cart item");
        }

        cartItem.setQuantity(request.getQuantity());
        CartItem updated = cartItemRepository.save(cartItem);

        return mapToResponse(updated);
    }

    @Override
    public void removeFromCart(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + id));

        if(!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this cart item");
        }

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        List<CartItem> cartItems = cartItemRepository.findAllByUser(user);
        cartItemRepository.deleteAll(cartItems);
    }
}
