package com.backend.demoBackend.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.backend.demoBackend.model.Cart.CartProduct;

public class CartProductRowMapper implements RowMapper<CartProduct> {
    @Override
    public CartProduct mapRow(ResultSet rs, int rowNum)
            throws SQLException {
        CartProduct cartProduct = new CartProduct();
        cartProduct.setProductId(rs.getString("PRODUCT_ID"));
        cartProduct.setProdName(rs.getString("PRODUCT_NAME"));
        cartProduct.setProductBrand(rs.getString("PRODUCT_BRAND"));
        cartProduct.setQuantity(rs.getInt("CART_QUANTITY"));
        cartProduct.setMaxQuantity(rs.getInt("PRODUCT_MAX_QUANTITY"));
        cartProduct.setPrimary_image(rs.getString("PRODUCT_PRIMARY_IMAGE"));
        cartProduct.setPrice(rs.getInt("PRODUCT_PRICE"));

        return cartProduct;
    }
}
