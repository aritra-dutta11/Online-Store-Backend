package com.backend.demoBackend.repository;

import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.backend.demoBackend.RowMapper.CategoryRowMapper;
import com.backend.demoBackend.model.PicrdResponse;
import com.backend.demoBackend.model.Category.Category;
import com.backend.demoBackend.model.Category.CategoryCreateRequest;
import com.backend.demoBackend.model.Category.CategoryCreateResponse;
import com.backend.demoBackend.model.Category.GetCategoryResponse;

import oracle.jdbc.OracleTypes;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CategoryCreateResponse handleCreateCategory(CategoryCreateRequest categoryReq, PicrdResponse picrdResponse) {

        CategoryCreateResponse response = new CategoryCreateResponse();

        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("PKG_CATEGORY")
                    .withProcedureName("PROC_ADD_CATEGORY");

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("IO_CATNAME", categoryReq.getCategoryName())
                    .addValue("I_CATDESC", categoryReq.getCategoryDesc())
                    .addValue("I_IMG_PAGEURL", picrdResponse.getPage_url())
                    .addValue("I_IMG_URL", picrdResponse.getImage_url())
                    .addValue("I_IMG_DELETEURL", picrdResponse.getDelete_url());

            Map<String, Object> result = jdbcCall.execute(params);
            String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
            if (errMsg.isEmpty() || errMsg.equals("")) {
                response.setCategoryName((String) result.get("IO_CATNAME"));
                response.setCategoryId((String) result.get("O_CATID"));
            }
            response.serviceResult.setErrorMsg(errMsg);
            response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
        } catch (Exception e) {
            response.serviceResult
                    .setErrorMsg("Exception from handleCreateCategory - CategoryRepository -" + e.getMessage());
            response.serviceResult.setErrorCode("1");
        }

        return response;
    }

    public GetCategoryResponse handleGetCategories() {

        GetCategoryResponse response = new GetCategoryResponse();

        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("PKG_CATEGORY")
                    .withProcedureName("PROC_GET_ALL_CATEGORIES")
                    .declareParameters(
                            new SqlOutParameter(
                                    "O_CATCURSOR",
                                    OracleTypes.CURSOR,
                                    new CategoryRowMapper()),
                            new SqlOutParameter(
                                    "O_ERRMSG",
                                    Types.VARCHAR),
                            new SqlOutParameter(
                                    "O_ERRCODE",
                                    Types.VARCHAR));

            Map<String, Object> result = jdbcCall.execute();
            String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
            // System.out.println("errMsg = " + errMsg);

            if (errMsg.isEmpty() || errMsg.equals("")) {
                List<Category> categories = (List<Category>) result.get("O_CATCURSOR");
                response.categoryList = categories;
            }

            response.serviceResult.setErrorMsg(errMsg);
            response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
            // System.out.println(response);

        } catch (Exception e) {
            response.serviceResult
                    .setErrorMsg("Exception from handleGetCategory - CategoryRepository -" + e.getMessage());
            response.serviceResult.setErrorCode("1");
        }

        return response;
    }
}
