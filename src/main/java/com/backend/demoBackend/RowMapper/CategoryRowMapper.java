package com.backend.demoBackend.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.backend.demoBackend.model.Category.Category;

public class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet rs, int rowNum)
                        throws SQLException {

                Category category = new Category();

                category.setCategoryId(
                                rs.getString("CATEGORYID"));

                category.setCategoryName(
                                rs.getString("CATEGORYNAME"));
                category.setCategoryImage(
                                rs.getString("IMAGE_URL"));

                return category;
        }
}
