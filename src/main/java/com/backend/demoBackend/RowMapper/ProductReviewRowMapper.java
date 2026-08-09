package com.backend.demoBackend.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.backend.demoBackend.model.Category.Category;
import com.backend.demoBackend.model.Product.Product;
import com.backend.demoBackend.model.Product.ProductReview;

public class ProductReviewRowMapper implements RowMapper<ProductReview> {
        @Override
        public ProductReview mapRow(ResultSet rs, int rowNum)
                        throws SQLException {

                ProductReview productReview = new ProductReview();

                productReview.setComment(
                                rs.getString("REVIEW_COMMENT"));

                productReview.setRating(
                                rs.getDouble("REVIEW_RATING"));

                productReview.setReviewId(
                                rs.getString("REVIEW_ID"));

                productReview.setUserName(
                                rs.getString("USER_NAME"));

                return productReview;
        }
}
