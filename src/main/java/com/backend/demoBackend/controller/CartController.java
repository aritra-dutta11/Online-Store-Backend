package com.backend.demoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.demoBackend.model.Cart.AddToCartRequest;
import com.backend.demoBackend.model.Cart.AddToCartResponse;
import com.backend.demoBackend.model.Cart.GetCartResponse;
import com.backend.demoBackend.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    CartService cartServ;

    @PostMapping("/add")
    public ResponseEntity<AddToCartResponse> addToCart(Authentication auth, @RequestBody AddToCartRequest req) {

        AddToCartResponse cartRes = cartServ.addToCart(req, auth.getName());

        if ("".equals(cartRes.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(cartRes);
        }
        return ResponseEntity.ok().body(cartRes);
    }

    @GetMapping("/getCart")
    public ResponseEntity<GetCartResponse> getCart(Authentication auth) {
        // System.out.println(prodRequest.toString());
        // System.out.println(pageNo);
        System.out.println(auth.getName());
        GetCartResponse response = cartServ.getCart(auth.getName());

        if ("".equals(response.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok().body(response);
    }
}
