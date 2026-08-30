package com.backend.demoBackend.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.backend.demoBackend.Exception.PlaceOrderException;
import com.backend.demoBackend.model.Order.GetCheckoutDetailsRequest;
import com.backend.demoBackend.model.Order.GetCheckoutDetailsResponse;
import com.backend.demoBackend.model.Order.PlaceOrderRequest;
import com.backend.demoBackend.model.Order.PlaceOrderResponse;
import com.backend.demoBackend.repository.CartRepository;
import com.backend.demoBackend.repository.OrderRepository;
import com.backend.demoBackend.repository.ProductRepository;
import com.backend.demoBackend.repository.UserRepository;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepo;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    CartRepository cartRepo;

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

    @Transactional
    public PlaceOrderResponse placeOrder(PlaceOrderRequest req, String userId) {
        PlaceOrderResponse orderRes = new PlaceOrderResponse();
        boolean isRollbackRequired = false;
        String transactionId = "";
        double totalPayableAmount = 0.0;
        try {

            // Populate Order Details and Order Items
            orderRepo.handlePopulateOrderDetails(req, orderRes, userId);
            System.out.println(orderRes.serviceResult.isSuccess());

            if (!orderRes.serviceResult.isSuccess()) {
                throw new PlaceOrderException(orderRes.serviceResult.getErrorMsg(),
                        orderRes.serviceResult.getErrorCode());
            }

            totalPayableAmount = orderRes.getNetAmt() + orderRes.getShippingCharges();

            // Update product stock in DB
            productRepo.handleUpdateProductStock(req, orderRes, userId);
            System.out.println(orderRes.serviceResult.isSuccess());

            if (!orderRes.serviceResult.isSuccess()) {
                throw new PlaceOrderException(orderRes.serviceResult.getErrorMsg(),
                        orderRes.serviceResult.getErrorCode());
            }

            // Delete Cart Data
            cartRepo.handleRemoveCartAfterOrder(req, orderRes, userId);
            System.out.println(orderRes.serviceResult.isSuccess());

            if (!orderRes.serviceResult.isSuccess()) {
                throw new PlaceOrderException(orderRes.serviceResult.getErrorMsg(),
                        orderRes.serviceResult.getErrorCode());
            }
            String paymentMode = req.getPaymentMode();

            switch (paymentMode) {
                case "cod":

                    break;
                case "wallet":
                    // Deduct money from wallet

                    userRepo.handleUpdateWallet(req, orderRes, userId, totalPayableAmount);
                    if (!orderRes.serviceResult.isSuccess()) {
                        throw new PlaceOrderException(orderRes.serviceResult.getErrorMsg(),
                                orderRes.serviceResult.getErrorCode());
                    }
                    orderRepo.handleUpdatePaymentStatus(orderRes, userId, "S");

                    if (!orderRes.serviceResult.isSuccess()) {
                        throw new PlaceOrderException(orderRes.serviceResult.getErrorMsg(),
                                orderRes.serviceResult.getErrorCode());
                    }
                    LocalDateTime date = java.time.LocalDateTime.now();
                    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    transactionId = "TXN" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "000"
                            + date.format(DateTimeFormatter.ofPattern("HHmmssSSS"));
                    break;

                case "upi":

                    break;
                case "card":

                    break;

                case "bank":

                    break;

                default:
                    break;
            }

            if (!orderRes.serviceResult.isSuccess()) {
                throw new PlaceOrderException(orderRes.serviceResult.getErrorMsg(),
                        orderRes.serviceResult.getErrorCode());
            }

            if (!paymentMode.equals("cod")) {
                orderRepo.handleSaveTransactionDetails(orderRes, transactionId, "", totalPayableAmount);
            }
            System.out.println(transactionId + " " + totalPayableAmount);

        } catch (PlaceOrderException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            orderRes.serviceResult
                    .setErrorMsg("PlaceOrderException from placeOrder - OrderService : " + e.getMessage());
            orderRes.serviceResult.setErrorCode(e.getErrorCode());
            orderRes.serviceResult.setSuccess(false);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            orderRes.serviceResult.setErrorMsg("Exception from placeOrder - OrderService : " + e.getMessage());
            orderRes.serviceResult.setErrorCode("1");
            orderRes.serviceResult.setSuccess(false);
        }
        if (isRollbackRequired) {

        }
        return orderRes;
    }
}
