package com.backend.demoBackend.repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.backend.demoBackend.model.Order.GetCheckoutDetailsRequest;
import com.backend.demoBackend.model.Order.GetCheckoutDetailsResponse;
import com.backend.demoBackend.model.Order.PlaceOrderRequest;
import com.backend.demoBackend.model.Order.PlaceOrderResponse;

@Repository
public class OrderRepository {
        private final JdbcTemplate jdbcTemplate;

        public OrderRepository(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        public GetCheckoutDetailsResponse handleGetCheckoutDetails(GetCheckoutDetailsRequest req, String userId) {
                GetCheckoutDetailsResponse response = new GetCheckoutDetailsResponse();
                System.out.println("Req - " + req.toString());
                System.out.println(userId);
                try {
                        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                                        .withCatalogName("PKG_ORDERS")
                                        .withProcedureName("PROC_GET_CHECKOUT_DETAILS")
                                        .declareParameters(
                                                        new SqlParameter(
                                                                        "I_USERID",
                                                                        Types.VARCHAR),

                                                        new SqlParameter(
                                                                        "I_CARTID",
                                                                        Types.VARCHAR),

                                                        new SqlOutParameter(
                                                                        "O_TOTAL_AMT",
                                                                        Types.NUMERIC),

                                                        new SqlOutParameter(
                                                                        "O_SHIPPING",
                                                                        Types.NUMERIC),

                                                        new SqlOutParameter(
                                                                        "O_SHIPPING_PRICE_LIMIT",
                                                                        Types.NUMERIC),
                                                        new SqlOutParameter(
                                                                        "O_ERRMSG",
                                                                        Types.VARCHAR),
                                                        new SqlOutParameter(
                                                                        "O_ERRCODE",
                                                                        Types.VARCHAR));

                        SqlParameterSource params = new MapSqlParameterSource()
                                        .addValue("I_USERID", userId)
                                        .addValue("I_CARTID", req.getCartId());

                        Map<String, Object> result = new HashMap<String, Object>();
                        try {
                                result = jdbcCall.execute(params);
                                System.out.println(result);
                        } catch (DataAccessException e) {
                                // System.out.println(" Exception while - " + e.getMessage());
                        }
                        // System.out.println(result);
                        String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
                        // System.out.println("errMsg = " + errMsg);

                        if (errMsg.isEmpty() || errMsg.equals("")) {
                                // System.out.println("Here");
                                // System.out.println(result.get("O_REV_CURSOR"));

                                response.serviceResult.setSuccess(true);
                                response.setTotalAmt(((Number) (result.get("O_TOTAL_AMT"))).doubleValue());
                                response.setShippingCharges(((Number) (result.get("O_SHIPPING"))).doubleValue());
                                response.setShippingChargesLimit(
                                                ((Number) (result.get("O_SHIPPING_PRICE_LIMIT"))).doubleValue());

                        }

                        response.serviceResult.setErrorMsg(errMsg);
                        response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
                        // System.out.println(response);
                } catch (Exception e) {
                        response.serviceResult
                                        .setErrorMsg("Exception from handleGetCheckoutDetails -OrderRepository -"
                                                        + e.getMessage());
                        response.serviceResult.setErrorCode("1");
                        response.serviceResult.setSuccess(false);
                }
                return response;
        }

        public void handlePopulateOrderDetails(PlaceOrderRequest request, PlaceOrderResponse response, String userId) {
                System.out.println("Req - " + request.toString());
                System.out.println(userId);
                try {
                        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                                        .withCatalogName("PKG_ORDERS")
                                        .withProcedureName("PROC_POPULATE_ORDER_DETAILS")
                                        .declareParameters(
                                                        new SqlParameter(
                                                                        "I_USER_ID",
                                                                        Types.VARCHAR),

                                                        new SqlParameter(
                                                                        "I_CARTID",
                                                                        Types.VARCHAR),
                                                        new SqlParameter(
                                                                        "I_ADDRESS_ID",
                                                                        Types.VARCHAR),
                                                        new SqlParameter(
                                                                        "I_PAYMENT_MODE",
                                                                        Types.VARCHAR),

                                                        new SqlOutParameter(
                                                                        "O_TOTAL_AMT",
                                                                        Types.NUMERIC),

                                                        new SqlOutParameter(
                                                                        "O_SHIPPING_CHARGES",
                                                                        Types.NUMERIC),

                                                        new SqlOutParameter(
                                                                        "O_ORDER_ID",
                                                                        Types.VARCHAR),
                                                        new SqlOutParameter(
                                                                        "O_ERRMSG",
                                                                        Types.VARCHAR),
                                                        new SqlOutParameter(
                                                                        "O_ERRCODE",
                                                                        Types.VARCHAR));

                        SqlParameterSource params = new MapSqlParameterSource()
                                        .addValue("I_USER_ID", userId)
                                        .addValue("I_CARTID", request.getCartId())
                                        .addValue("I_ADDRESS_ID", request.getAddressId())
                                        .addValue("I_PAYMENT_MODE", request.getPaymentMode());

                        Map<String, Object> result = new HashMap<String, Object>();
                        try {
                                result = jdbcCall.execute(params);
                                System.out.println(result);
                        } catch (DataAccessException e) {
                                // System.out.println(" Exception while - " + e.getMessage());
                        }
                        // System.out.println(result);
                        String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
                        // System.out.println("errMsg = " + errMsg);

                        if (errMsg.isEmpty() || errMsg.equals("")) {
                                // System.out.println("Here");
                                // System.out.println(result.get("O_REV_CURSOR"));

                                response.serviceResult.setSuccess(true);
                                response.setNetAmt(((Number) (result.get("O_TOTAL_AMT"))).doubleValue());
                                response.setShippingCharges(
                                                ((Number) (result.get("O_SHIPPING_CHARGES"))).doubleValue());
                                response.setOrderId((String) (result.get("O_ORDER_ID")));
                                response.serviceResult.setSuccess(true);

                        } else {
                                response.serviceResult.setSuccess(false);
                        }

                        response.serviceResult.setErrorMsg(errMsg);
                        response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
                        // System.out.println(response);
                } catch (Exception e) {
                        response.serviceResult
                                        .setErrorMsg("Exception from handleGetCheckoutDetails -OrderRepository -"
                                                        + e.getMessage());
                        response.serviceResult.setErrorCode("1");
                        response.serviceResult.setSuccess(false);
                }
        }

        public void handleWalletTransaction(PlaceOrderRequest request, PlaceOrderResponse response, String userId) {
                System.out.println("Req - " + request.toString());
                System.out.println(userId);
                try {
                        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                                        .withCatalogName("PKG_ORDERS")
                                        .withProcedureName("PROC_POPULATE_ORDER_DETAILS");

                        SqlParameterSource params = new MapSqlParameterSource()
                                        .addValue("I_USER_ID", userId)
                                        .addValue("I_CARTID", request.getCartId())
                                        .addValue("I_ADDRESS_ID", request.getAddressId())
                                        .addValue("I_PAYMENT_MODE", request.getPaymentMode());

                        Map<String, Object> result = new HashMap<String, Object>();
                        try {
                                result = jdbcCall.execute(params);
                                System.out.println(result);
                        } catch (DataAccessException e) {
                                // System.out.println(" Exception while - " + e.getMessage());
                        }
                        // System.out.println(result);
                        String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
                        // System.out.println("errMsg = " + errMsg);

                        if (errMsg.isEmpty() || errMsg.equals("")) {
                                // System.out.println("Here");
                                // System.out.println(result.get("O_REV_CURSOR"));

                                response.serviceResult.setSuccess(true);
                                response.setNetAmt(((Number) (result.get("O_TOTAL_AMT"))).doubleValue());
                                response.setShippingCharges(
                                                ((Number) (result.get("O_SHIPPING_CHARGES"))).doubleValue());
                                response.setOrderId((String) (result.get("O_ORDER_ID")));

                        }

                        response.serviceResult.setErrorMsg(errMsg);
                        response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
                        // System.out.println(response);
                } catch (Exception e) {
                        response.serviceResult
                                        .setErrorMsg("Exception from handleWalletTransaction -OrderRepository -"
                                                        + e.getMessage());
                        response.serviceResult.setErrorCode("1");
                        response.serviceResult.setSuccess(false);
                }
        }

        public void handleSaveTransactionDetails(PlaceOrderResponse response,
                        String transactionId,
                        String paymentFailureRemarks, double totalPayableAmt) {

                try {
                        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                                        .withCatalogName("PKG_ORDERS")
                                        .withProcedureName("PROC_INSERT_TRANSACTION_DETAILS");

                        SqlParameterSource params = new MapSqlParameterSource()
                                        .addValue("I_ORDER_ID", response.getOrderId())
                                        .addValue("I_TRANSACTION_ID", transactionId)
                                        .addValue("I_TOTAL_PAYABLE_AMT", totalPayableAmt)
                                        .addValue("I_PAYMENT_FAILURE_REMARKS", paymentFailureRemarks);

                        Map<String, Object> result = new HashMap<String, Object>();
                        try {
                                result = jdbcCall.execute(params);
                                System.out.println(result);
                        } catch (DataAccessException e) {
                                System.out.println(" Exception while - " + e.getMessage());
                        }
                        System.out.println(result);
                        String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
                        // System.out.println("errMsg = " + errMsg);

                        if (errMsg.isEmpty() || errMsg.equals("")) {
                                // System.out.println("Here");
                                // System.out.println(result.get("O_REV_CURSOR"));

                                response.serviceResult.setSuccess(true);

                        } else {
                                response.serviceResult.setSuccess(false);
                        }

                        response.serviceResult.setErrorMsg(errMsg);
                        response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
                        // System.out.println(response);
                } catch (Exception e) {
                        response.serviceResult
                                        .setErrorMsg("Exception from handleSaveTransactionDetails -OrderRepository -"
                                                        + e.getMessage());
                        response.serviceResult.setErrorCode("1");
                        response.serviceResult.setSuccess(false);
                }

        }

        public void handleUpdatePaymentStatus(PlaceOrderResponse response, String userId, String paymentStatus) {

                try {
                        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                                        .withCatalogName("PKG_ORDERS")
                                        .withProcedureName("PROC_UPDATE_PAYMENT_STATUS");

                        SqlParameterSource params = new MapSqlParameterSource()
                                        .addValue("I_USER_ID", userId)
                                        .addValue("I_ORDER_ID", response.getOrderId())
                                        .addValue("I_PAYMENT_STATUS", paymentStatus);

                        Map<String, Object> result = new HashMap<String, Object>();
                        try {
                                result = jdbcCall.execute(params);
                                System.out.println(result);
                        } catch (DataAccessException e) {
                                // System.out.println(" Exception while - " + e.getMessage());
                        }
                        // System.out.println(result);
                        String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
                        // System.out.println("errMsg = " + errMsg);

                        if (errMsg.isEmpty() || errMsg.equals("")) {
                                // System.out.println("Here");
                                // System.out.println(result.get("O_REV_CURSOR"));

                                response.serviceResult.setSuccess(true);

                        } else {
                                response.serviceResult.setSuccess(false);
                        }

                        response.serviceResult.setErrorMsg(errMsg);
                        response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
                        // System.out.println(response);
                } catch (Exception e) {
                        response.serviceResult
                                        .setErrorMsg("Exception from handleSaveTransactionDetails -OrderRepository -"
                                                        + e.getMessage());
                        response.serviceResult.setErrorCode("1");
                        response.serviceResult.setSuccess(false);
                }
        }
}
