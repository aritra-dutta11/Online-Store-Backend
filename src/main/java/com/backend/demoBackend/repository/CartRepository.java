package com.backend.demoBackend.repository;

import java.util.Map;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.backend.demoBackend.model.Cart.AddToCartRequest;
import com.backend.demoBackend.model.Cart.AddToCartResponse;

@Repository
public class CartRepository {
    private final JdbcTemplate jdbcTemplate;

    public CartRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AddToCartResponse handleAddToCart(AddToCartRequest req, String userId) {
        AddToCartResponse response = new AddToCartResponse();

        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("PKG_PRODUCTS")
                    .withProcedureName("PROC_ADD_TO_CART");

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("IO_PRODID", req.getProdId())
                    .addValue("IO_USERID", userId);

            Map<String, Object> result = jdbcCall.execute(params);
            String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
            if (errMsg.isEmpty() || errMsg.equals("")) {
                response.setProdId((String) result.get("IO_PRODID"));
                response.setCartId((String) result.get("O_CARTID"));
            }
            response.serviceResult.setErrorMsg(errMsg);
            response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
        } catch (Exception e) {
            response.serviceResult.setErrorMsg("Exception from createNewUser - UserRepository -" + e.getMessage());
            response.serviceResult.setErrorCode("1");
        }

        return response;
    }

}
