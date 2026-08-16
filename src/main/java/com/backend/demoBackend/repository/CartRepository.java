package com.backend.demoBackend.repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.backend.demoBackend.RowMapper.CartProductRowMapper;
import com.backend.demoBackend.model.Cart.AddToCartRequest;
import com.backend.demoBackend.model.Cart.AddToCartResponse;
import com.backend.demoBackend.model.Cart.CartProduct;
import com.backend.demoBackend.model.Cart.GetCartResponse;

import oracle.jdbc.OracleTypes;

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

    public GetCartResponse handleGetCart(String userId) {
        GetCartResponse response = new GetCartResponse();

        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("PKG_PRODUCTS")
                    .withProcedureName("PROC_GET_CART")
                    .declareParameters(
                            new SqlInOutParameter(
                                    "IO_USERID",
                                    Types.VARCHAR),
                            new SqlOutParameter(
                                    "O_CART_CURSOR",
                                    OracleTypes.CURSOR,

                                    new CartProductRowMapper()),
                            new SqlOutParameter(
                                    "O_CARTID",
                                    Types.VARCHAR),
                            new SqlOutParameter(
                                    "O_ERRMSG",
                                    Types.VARCHAR),
                            new SqlOutParameter(
                                    "O_ERRCODE",
                                    Types.VARCHAR));

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("IO_USERID", userId);

            Map<String, Object> result = new HashMap<String, Object>();
            try {
                System.out.println("Hello 1");

                result = jdbcCall.execute(params);
                System.out.println("Hello 3");

            } catch (DataAccessException e) {
                System.out.println("Hello 2");
                System.out.println(e.getLocalizedMessage());
            }

            String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
            // System.out.println("errMsg = " + errMsg);

            if (errMsg.isEmpty() || errMsg.equals("")) {
                // System.out.println("Here");
                // System.out.println(result.get("O_PROD_CURSOR"));
                List<CartProduct> cartProducts = (List<CartProduct>) result.get("O_CART_CURSOR");
                response.cartProductList = cartProducts;
                response.setCartId((String) result.get("O_CARTID"));
            }

            response.serviceResult.setErrorMsg(errMsg);
            response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
            System.out.println(response);

        } catch (Exception e) {
            response.serviceResult
                    .setErrorMsg("Exception from handleGetCart - ProductRepository -"
                            + e.getMessage());
            response.serviceResult.setErrorCode("1");
        }

        return response;

    }

}
