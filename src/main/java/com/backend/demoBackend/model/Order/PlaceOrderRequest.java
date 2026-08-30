package com.backend.demoBackend.model.Order;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PlaceOrderRequest {
    private String paymentMode;
    private String cartId;
    private String addressId;
    public WalletDetails walletDetails = new WalletDetails();
}
