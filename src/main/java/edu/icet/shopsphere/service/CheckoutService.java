package edu.icet.shopsphere.service;

import edu.icet.shopsphere.dto.checkout.CheckoutRequest;
import edu.icet.shopsphere.dto.order.OrderResponse;

public interface CheckoutService {
    OrderResponse checkout(CheckoutRequest request);
}
