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

import com.backend.demoBackend.RowMapper.ProductReviewRowMapper;
import com.backend.demoBackend.model.Order.GetCheckoutDetailsRequest;
import com.backend.demoBackend.model.Order.GetCheckoutDetailsResponse;
import com.backend.demoBackend.model.Product.ProductReview;

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
                                        .withCatalogName("PKG_PRODUCTS")
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
}
