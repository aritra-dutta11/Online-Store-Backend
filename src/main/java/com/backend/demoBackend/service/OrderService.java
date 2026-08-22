package com.backend.demoBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.demoBackend.model.Order.GetCheckoutDetailsRequest;
import com.backend.demoBackend.model.Order.GetCheckoutDetailsResponse;
import com.backend.demoBackend.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepo;

    public GetCheckoutDetailsResponse getCheckoutDetails(GetCheckoutDetailsRequest req, String userId) {
        GetCheckoutDetailsResponse orderRes = new GetCheckoutDetailsResponse();
        try {
            orderRes = orderRepo.handleGetCheckoutDetails(req, userId);
        } catch (Exception e) {
            orderRes.serviceResult.setErrorMsg("Exception from getCheckoutDetails - OrderService : " + e.getMessage());
            orderRes.serviceResult.setErrorCode("1");
            orderRes.serviceResult.setSuccess(false);
        }
        return orderRes;
    }
}
