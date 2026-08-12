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
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.backend.demoBackend.RowMapper.ProductReviewRowMapper;
import com.backend.demoBackend.RowMapper.ProductRowMapper;
import com.backend.demoBackend.model.PicrdResponse;
import com.backend.demoBackend.model.Product.CreateProductRequest;
import com.backend.demoBackend.model.Product.CreateProductResponse;
import com.backend.demoBackend.model.Product.FilterRequest;
import com.backend.demoBackend.model.Product.GetProductResponse;
import com.backend.demoBackend.model.Product.GetSingleProductResponse;
import com.backend.demoBackend.model.Product.Product;
import com.backend.demoBackend.model.Product.ProductReview;
import com.backend.demoBackend.model.Product.ProductReviewRequest;
import com.backend.demoBackend.model.Product.ProductReviewResponse;

import oracle.jdbc.OracleTypes;

@Repository
public class ProductRepository {
        private final JdbcTemplate jdbcTemplate;

        public ProductRepository(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        public CreateProductResponse createNewProduct(CreateProductRequest prodRequest,
                        List<PicrdResponse> picrdResponseList) {
                CreateProductResponse response = new CreateProductResponse();

                try {
                        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("PKG_PRODUCTS")
                                        .withProcedureName("PROC_ADD_NEW_PRODUCT");

                        SqlParameterSource params = new MapSqlParameterSource()
                                        .addValue("IO_PRODNAME", prodRequest.getProductName())
                                        .addValue("IO_PRODDESC", prodRequest.getProductDesc())
                                        .addValue("I_QUANTITY", prodRequest.getQuantity())
                                        .addValue("I_PRICE", prodRequest.getPrice())
                                        .addValue("I_PRODCATEGORY", prodRequest.getCategoryId())
                                        .addValue("I_PRODBRAND", prodRequest.getProductBrand());

                        Map<String, Object> result = jdbcCall.execute(params);
                        String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
                        String productId = Objects.toString(result.get("O_PRODID"), "");
                        if ((errMsg.isEmpty() || errMsg.equals("")) && !productId.equals("")) {
                                errMsg += handleSaveProductImages(productId, picrdResponseList);
                        }
                        if (errMsg.isEmpty() || errMsg.equals("")) {
                                response.setProductName((String) result.get("IO_PRODNAME"));
                                response.setProductId((String) result.get("O_PRODID"));
                                response.setProductDesc((String) result.get("IO_PRODDESC"));
                        } else {
                                String sql = "DELETE FROM PRODUCT_IMAGES WHERE PRODUCTID = ?";

                                jdbcTemplate.update(sql, productId);

                                sql = "DELETE FROM PRODUCT WHERE PRODUCTID = ?";
                                jdbcTemplate.update(sql, productId);
                                // Delete the data from the table.
                        }
                        response.serviceResult.setErrorMsg(errMsg);
                        response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
                } catch (Exception e) {
                        response.serviceResult
                                        .setErrorMsg("Exception from createNewProduct - ProductRepository -"
                                                        + e.getMessage());
                        response.serviceResult.setErrorCode("1");
                }

                return response;
        }

        public String handleSaveProductImages(String productId, List<PicrdResponse> handleSaveProductImages) {
                String errMsg = "";
                String qry = """
                                INSERT INTO PRODUCT_IMAGES(IMAGEID, PRODUCTID, IMAGE_URL, IMAGE_PAGEURL, IMAGE_DELETEURL, IS_PRIMARY_IMAGE, CREATEDAT)
                                VALUES (?,?,?,?,?,?,SYSDATE)
                                """;
                try {
                        jdbcTemplate.batchUpdate(qry, handleSaveProductImages, handleSaveProductImages.size(),
                                        (ps, data) -> {
                                                ps.setString(1, data.getImage_id());
                                                ps.setString(2, productId);
                                                ps.setString(3, data.getImage_url());
                                                ps.setString(4, data.getPage_url());
                                                ps.setString(5, data.getDelete_url());
                                                ps.setString(6, handleSaveProductImages.indexOf(data) == 0 ? "Y" : "N");
                                        });
                } catch (Exception e) {
                        errMsg += " Exception from handleSaveProductImages - " + e.getMessage();
                }

                return errMsg;
        }

        public GetProductResponse handleGetProducts(int pageNo) {
                GetProductResponse response = new GetProductResponse();

                try {
                        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                                        .withCatalogName("PKG_PRODUCTS")
                                        .withProcedureName("PROC_GET_PRODUCTS")
                                        .declareParameters(
                                                        new SqlParameter(
                                                                        "I_PAGENO",
                                                                        Types.NUMERIC),
                                                        new SqlOutParameter(
                                                                        "O_PRODUCT_CURSOR",
                                                                        OracleTypes.CURSOR,

                                                                        new ProductRowMapper()),
                                                        new SqlOutParameter(
                                                                        "O_ERRMSG",
                                                                        Types.VARCHAR),
                                                        new SqlOutParameter(
                                                                        "O_ERRCODE",
                                                                        Types.VARCHAR));

                        SqlParameterSource params = new MapSqlParameterSource()
                                        .addValue(
                                                        "I_PAGENO",
                                                        pageNo);

                        Map<String, Object> result = new HashMap<String, Object>();
                        try {
                                result = jdbcCall.execute(params);
                        } catch (DataAccessException e) {
                                System.out.println(e.getMessage());
                        }

                        String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
                        // System.out.println("errMsg = " + errMsg);

                        if (errMsg.isEmpty() || errMsg.equals("")) {
                                // System.out.println("Here");
                                // System.out.println(result.get("O_PRODUCT_CURSOR"));
                                List<Product> products = (List<Product>) result.get("O_PRODUCT_CURSOR");
                                response.prodList = products;
                        }

                        response.serviceResult.setErrorMsg(errMsg);
                        response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
                        // System.out.println(response);

                } catch (Exception e) {
                        response.serviceResult
                                        .setErrorMsg("Exception from handleGetProducts - ProductRepository -"
                                                        + e.getMessage());
                        response.serviceResult.setErrorCode("1");
                }

                return response;

        }

        public GetProductResponse handleGetProducts(String searchKey, int pageNo, FilterRequest filters) {
                GetProductResponse response = new GetProductResponse();
                System.out.println(searchKey + " " + pageNo);

                try {
                        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                                        .withCatalogName("PKG_PRODUCTS")
                                        .withProcedureName("PROC_SEARCH_PRODUCTS")
                                        .declareParameters(
                                                        new SqlParameter(
                                                                        "I_PAGENO",
                                                                        Types.NUMERIC),
                                                        new SqlParameter(
                                                                        "I_SEARCH_KEY",
                                                                        Types.VARCHAR),
                                                        new SqlParameter(
                                                                        "I_MINPRICE",
                                                                        Types.NUMERIC),
                                                        new SqlParameter(
                                                                        "I_MAXPRICE",
                                                                        Types.NUMERIC),
                                                        new SqlParameter(
                                                                        "I_CATEGORYID",
                                                                        Types.NUMERIC),
                                                        new SqlParameter(
                                                                        "I_RATING",
                                                                        Types.NUMERIC),
                                                        new SqlOutParameter(
                                                                        "O_PROD_CURSOR",
                                                                        OracleTypes.CURSOR,

                                                                        new ProductRowMapper()),
                                                        new SqlOutParameter(
                                                                        "O_ERRMSG",
                                                                        Types.VARCHAR),
                                                        new SqlOutParameter(
                                                                        "O_ERRCODE",
                                                                        Types.VARCHAR));

                        SqlParameterSource params = new MapSqlParameterSource()
                                        .addValue("I_PAGENO", pageNo)
                                        .addValue("I_SEARCH_KEY", searchKey)
                                        .addValue("I_MINPRICE", filters.getMinPrice())
                                        .addValue("I_MAXPRICE", filters.getMaxPrice())
                                        .addValue("I_CATEGORYID", filters.getCategoryId())
                                        .addValue("I_RATING", filters.getRating());

                        Map<String, Object> result = new HashMap<String, Object>();
                        try {
                                result = jdbcCall.execute(params);
                        } catch (DataAccessException e) {
                                System.out.println(e.getMessage());
                        }

                        String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
                        // System.out.println("errMsg = " + errMsg);

                        if (errMsg.isEmpty() || errMsg.equals("")) {
                                // System.out.println("Here");
                                System.out.println(result.get("O_PROD_CURSOR"));
                                List<Product> products = (List<Product>) result.get("O_PROD_CURSOR");
                                response.prodList = products;
                        }

                        response.serviceResult.setErrorMsg(errMsg);
                        response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
                        System.out.println(response);

                } catch (Exception e) {
                        response.serviceResult
                                        .setErrorMsg("Exception from handleGetProducts - ProductRepository -"
                                                        + e.getMessage());
                        response.serviceResult.setErrorCode("1");
                }

                return response;

        }

        public ProductReviewResponse handleAddNewReview(ProductReviewRequest prodRequest, String userId) {
                ProductReviewResponse response = new ProductReviewResponse();

                try {
                        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("PKG_PRODUCTS")
                                        .withProcedureName("PROC_SAVE_REVIEWS");

                        SqlParameterSource params = new MapSqlParameterSource()
                                        .addValue("IO_PRODID", prodRequest.getProductId())
                                        .addValue("IO_USERID", userId)
                                        .addValue("IO_COMMENT", prodRequest.getComment())
                                        .addValue("IO_RATING", prodRequest.getRating());

                        Map<String, Object> result = jdbcCall.execute(params);
                        String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
                        if (errMsg.isEmpty() || errMsg.equals("")) {
                                response.setProductId((String) result.get("IO_PRODID"));
                                response.setUserId((String) result.get("IO_USERID"));
                                response.setComment((String) result.get("IO_COMMENT"));
                                response.setRating(((Number) result.get("IO_RATING")).doubleValue());
                                response.setReviewId((String) result.get("O_REVIEWID"));
                        }
                        response.serviceResult.setErrorMsg(errMsg);
                        response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
                } catch (Exception e) {
                        response.serviceResult
                                        .setErrorMsg("Exception from createNewProduct - ProductRepository -"
                                                        + e.getMessage());
                        response.serviceResult.setErrorCode("1");
                }

                return response;
        }

        public GetSingleProductResponse handleGetSingleProduct(String prodId) {
                GetSingleProductResponse response = new GetSingleProductResponse();

                try {
                        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                                        .withCatalogName("PKG_PRODUCTS")
                                        .withProcedureName("PROC_GET_PRODUCT")
                                        .declareParameters(
                                                        new SqlInOutParameter(
                                                                        "IO_PRODID",
                                                                        Types.VARCHAR),

                                                        new SqlOutParameter(
                                                                        "O_PRODNAME",
                                                                        Types.VARCHAR),

                                                        new SqlOutParameter(
                                                                        "O_PRICE",
                                                                        Types.NUMERIC),

                                                        new SqlOutParameter(
                                                                        "O_QUANTITY",
                                                                        Types.NUMERIC),

                                                        new SqlOutParameter(
                                                                        "O_PRODDESC",
                                                                        Types.VARCHAR),

                                                        new SqlOutParameter(
                                                                        "O_CATEGORY",
                                                                        Types.VARCHAR),

                                                        new SqlOutParameter(
                                                                        "O_AVGRATING",
                                                                        Types.NUMERIC),

                                                        new SqlOutParameter(
                                                                        "O_REV_CURSOR",
                                                                        OracleTypes.CURSOR,

                                                                        new ProductReviewRowMapper()),
                                                        new SqlOutParameter(
                                                                        "O_ERRMSG",
                                                                        Types.VARCHAR),
                                                        new SqlOutParameter(
                                                                        "O_ERRCODE",
                                                                        Types.VARCHAR));

                        SqlParameterSource params = new MapSqlParameterSource()
                                        .addValue(
                                                        "IO_PRODID",
                                                        prodId);

                        Map<String, Object> result = new HashMap<String, Object>();
                        try {
                                result = jdbcCall.execute(params);
                        } catch (DataAccessException e) {
                                // System.out.println(" Exception while - " + e.getMessage());
                        }
                        // System.out.println(result);
                        String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
                        // System.out.println("errMsg = " + errMsg);

                        if (errMsg.isEmpty() || errMsg.equals("")) {
                                // System.out.println("Here");
                                System.out.println(result.get("O_REV_CURSOR"));
                                List<ProductReview> productReviews = (List<ProductReview>) result.get("O_REV_CURSOR");
                                response.productDetails.reviews = productReviews;
                                response.productDetails.setProductId((String) result.get("IO_PRODID"));
                                response.productDetails.setProdName((String) result.get("O_PRODNAME"));
                                response.productDetails.setPrice(((Number) result.get("O_PRICE")).intValue());
                                response.productDetails.setProdDesc((String) result.get("O_PRODDESC"));
                                response.productDetails.setCategory((String) result.get("O_CATEGORY"));
                                response.productDetails
                                                .setAvgRating(((Number) result.get("O_AVGRATING")).doubleValue());
                        }

                        response.serviceResult.setErrorMsg(errMsg);
                        response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
                        System.out.println(response);

                } catch (Exception e) {
                        response.serviceResult
                                        .setErrorMsg("Exception from handleGetSingleProduct - ProductRepository -"
                                                        + e.getMessage());
                        response.serviceResult.setErrorCode("1");
                }

                return response;

        }
}
