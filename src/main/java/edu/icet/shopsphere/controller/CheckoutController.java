package edu.icet.shopsphere.controller;

import edu.icet.shopsphere.dto.checkout.CheckoutRequest;
import edu.icet.shopsphere.dto.order.OrderResponse;
import edu.icet.shopsphere.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public OrderResponse checkout(@RequestBody CheckoutRequest request) {
        return checkoutService.checkout(request);
    }
}
