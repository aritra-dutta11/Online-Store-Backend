package com.backend.demoBackend.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.backend.demoBackend.model.Category.Category;
import com.backend.demoBackend.model.Product.Product;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        Product product = new Product();

        product.setProductId(
                rs.getString("PRODUCT_ID"));

        product.setProdName(rs.getString("PRODUCT_NAME"));

        product.setPrice(rs.getInt("PRODUCT_PRICE"));

        product.setProdDesc(rs.getString("PRODUCT_DESC"));

        product.setCategory(rs.getString("PRODUCT_CATEGORY"));

        product.setAvgRating(rs.getDouble("PRODUCT_RATING"));

        return product;
    }
}
