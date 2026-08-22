package com.backend.demoBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.demoBackend.model.Cart.AddToCartRequest;
import com.backend.demoBackend.model.Cart.AddToCartResponse;
import com.backend.demoBackend.model.Cart.DeleteCartRequest;
import com.backend.demoBackend.model.Cart.DeleteCartResponse;
import com.backend.demoBackend.model.Cart.GetCartResponse;
import com.backend.demoBackend.model.Cart.UpdateCartRequest;
import com.backend.demoBackend.model.Cart.UpdateCartResponse;
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
            cartRes.serviceResult.setErrorMsg("Exception from addToCart - CartService : " + e.getMessage());
            cartRes.serviceResult.setErrorCode("1");
        }

        return cartRes;
    }

    public GetCartResponse getCart(String userId) {
        GetCartResponse cartRes = new GetCartResponse();
        try {
            cartRes = cartRepo.handleGetCart(userId);
        } catch (Exception e) {
            cartRes.serviceResult.setErrorMsg("Exception from getCart - CartService : " + e.getMessage());
            cartRes.serviceResult.setErrorCode("1");
        }
        return cartRes;
    }

    public UpdateCartResponse updateCart(UpdateCartRequest req) {
        UpdateCartResponse cartRes = new UpdateCartResponse();
        try {
            cartRes = cartRepo.handleUpdateCart(req);
        } catch (Exception e) {
            cartRes.serviceResult.setErrorMsg("Exception from updateCart - CartService : " + e.getMessage());
            cartRes.serviceResult.setErrorCode("1");
            cartRes.serviceResult.setSuccess(false);
        }
        return cartRes;
    }

    public DeleteCartResponse deleteCart(DeleteCartRequest req, String userId) {
        DeleteCartResponse cartRes = new DeleteCartResponse();
        try {
            cartRes = cartRepo.handleDeleteCart(req, userId);
        } catch (Exception e) {
            cartRes.serviceResult.setErrorMsg("Exception from updateCart - CartService : " + e.getMessage());
            cartRes.serviceResult.setErrorCode("1");
            cartRes.serviceResult.setSuccess(false);
        }
        return cartRes;
    }
}
