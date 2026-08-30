package com.backend.demoBackend.model.Order;

import org.springframework.stereotype.Component;

import com.backend.demoBackend.model.Service.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PlaceOrderResponse {
    private String orderId;
    private double netAmt;
    private double shippingCharges;
    public ServiceResult serviceResult = new ServiceResult();
}
