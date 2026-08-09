package com.backend.demoBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.demoBackend.model.Cart.AddToCartRequest;
import com.backend.demoBackend.model.Cart.AddToCartResponse;
import com.backend.demoBackend.repository.CartRepository;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepo;

    public AddToCartResponse addToCart(AddToCartRequest req, String userId) {
        AddToCartResponse cartRes = new AddToCartResponse();
        try {
            cartRes = cartRepo.handleAddToCart(req, userId);
        } catch (Exception e) {
            cartRes.serviceResult.setErrorMsg("Exception from createCategory - CategoryService : " + e.getMessage());
            cartRes.serviceResult.setErrorCode("1");
        }

        return cartRes;
    }
}
