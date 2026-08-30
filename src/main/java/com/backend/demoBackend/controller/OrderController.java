package com.backend.demoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.demoBackend.model.Order.GetCheckoutDetailsRequest;
import com.backend.demoBackend.model.Order.GetCheckoutDetailsResponse;
import com.backend.demoBackend.model.Order.PlaceOrderRequest;
import com.backend.demoBackend.model.Order.PlaceOrderResponse;
import com.backend.demoBackend.service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("/get/checkout/details")
    public ResponseEntity<GetCheckoutDetailsResponse> getCheckoutDetails(Authentication auth,
            @RequestBody GetCheckoutDetailsRequest req) {

        GetCheckoutDetailsResponse orderRes = orderService.getCheckoutDetails(req, auth.getName());

        if ("".equals(orderRes.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(orderRes);
        }
        return ResponseEntity.ok().body(orderRes);
    }

    @PostMapping("/placeorder")
    public ResponseEntity<PlaceOrderResponse> placeOrder(Authentication auth,
            @RequestBody PlaceOrderRequest req) {

        PlaceOrderResponse orderRes = orderService.placeOrder(req, auth.getName());

        if ("".equals(orderRes.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(orderRes);
        }
        return ResponseEntity.ok().body(orderRes);
    }

}
