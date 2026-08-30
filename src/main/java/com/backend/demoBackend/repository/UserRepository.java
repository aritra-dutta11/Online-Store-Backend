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

import com.backend.demoBackend.RowMapper.AddressRowMapper;
import com.backend.demoBackend.RowMapper.CartProductRowMapper;
import com.backend.demoBackend.model.Cart.CartProduct;
import com.backend.demoBackend.model.Cart.GetCartResponse;
import com.backend.demoBackend.model.Order.PlaceOrderRequest;
import com.backend.demoBackend.model.Order.PlaceOrderResponse;
import com.backend.demoBackend.model.User.Address;
import com.backend.demoBackend.model.User.DeleteUserAddressRequest;
import com.backend.demoBackend.model.User.DeleteUserAddressResponse;
import com.backend.demoBackend.model.User.GetUserAddressResponse;
import com.backend.demoBackend.model.User.GetUserWalletResponse;
import com.backend.demoBackend.model.User.SaveUserAddressRequest;
import com.backend.demoBackend.model.User.SaveUserAddressResponse;
import com.backend.demoBackend.model.User.User;
import com.backend.demoBackend.model.User.UserLoginData;
import com.backend.demoBackend.model.User.UserLoginRequest;
import com.backend.demoBackend.model.User.UserRequest;
import com.backend.demoBackend.model.User.UserResponse;

import oracle.jdbc.OracleTypes;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserResponse createNewUser(UserRequest userRequest) {
        UserResponse response = new UserResponse();
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("PKG_USERS")
                    .withProcedureName("PROC_CREATE_NEW_USER");

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("IO_USERNAME", userRequest.getUserName())
                    .addValue("I_PASSWORD", userRequest.getPassword());

            Map<String, Object> result = jdbcCall.execute(params);
            String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
            if (errMsg.isEmpty() || errMsg.equals("")) {
                response.setUserName((String) result.get("IO_USERNAME"));
                response.setUserId((String) result.get("O_USERID"));
            }
            response.serviceResult.setErrorMsg(errMsg);
            response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
        } catch (Exception e) {
            response.serviceResult.setErrorMsg("Exception from createNewUser - UserRepository -" + e.getMessage());
            response.serviceResult.setErrorCode("1");
        }

        return response;
    }

    public void createUserWallet(UserResponse userRes) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("PKG_USERS")
                    .withProcedureName("PROC_CREATE_USER_WALLET");

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("I_USERID", userRes.getUserId());

            Map<String, Object> result = jdbcCall.execute(params);
            String errMsg = Objects.toString(result.get("O_ERRMSG"), "");

            userRes.serviceResult.setErrorMsg(errMsg);
            userRes.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
        } catch (Exception e) {
            userRes.serviceResult.setErrorMsg("Exception from createUserWallet - UserRepository -" + e.getMessage());
            userRes.serviceResult.setErrorCode("1");
        }

    }

    public GetUserWalletResponse handleGetWallet(String userId) {
        GetUserWalletResponse response = new GetUserWalletResponse();
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("PKG_USERS")
                    .withProcedureName("PROC_GET_USER_WALLET");

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("I_USERID", userId);

            Map<String, Object> result = jdbcCall.execute(params);
            String errMsg = Objects.toString(result.get("O_ERRMSG"), "");

            if (errMsg.isEmpty() || errMsg.equals("")) {
                response.setWalletId((String) result.get("O_WALLET_ID"));
                response.setAmount(((Number) result.get("O_AMOUNT")).doubleValue());
                response.serviceResult.setSuccess(true);
            } else {
                response.serviceResult.setSuccess(false);
            }

            response.serviceResult.setErrorMsg(errMsg);
            response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));

        } catch (Exception e) {
            response.serviceResult.setErrorMsg("Exception from handleGetWallet - UserRepository -" + e.getMessage());
            response.serviceResult.setErrorCode("1");
            response.serviceResult.setSuccess(false);
        }
        return response;
    }

    public UserLoginData handleUserLogin(UserLoginRequest userRequest) {
        UserLoginData response = new UserLoginData();
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("PKG_USERS")
                    .withProcedureName("PROC_USER_LOGIN");

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("IO_USERID", userRequest.getUserId());

            Map<String, Object> result = jdbcCall.execute(params);
            // System.out.println(result);
            String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
            if (errMsg.isEmpty() || errMsg.equals("")) {
                response.setUserName((String) result.get("O_USERNAME"));
                response.setUserId((String) result.get("IO_USERID"));
                response.setPassword((String) result.get("O_PASSWORD"));
            }
            response.serviceResult.setErrorMsg(errMsg);
            response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
        } catch (Exception e) {
            response.serviceResult.setErrorMsg("Exception from handleUserLogin - UserRepository -" + e.getMessage());
            response.serviceResult.setErrorCode("1");
        }

        return response;
    }

    public UserResponse createNewAdmin(UserRequest userRequest) {
        UserResponse response = new UserResponse();
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("PKG_USERS")
                    .withProcedureName("PROC_CREATE_NEW_ADMIN");

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("IO_USERNAME", userRequest.getUserName())
                    .addValue("I_PASSWORD", userRequest.getPassword());

            Map<String, Object> result = jdbcCall.execute(params);
            String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
            if (errMsg.isEmpty() || errMsg.equals("")) {
                response.setUserName((String) result.get("IO_USERNAME"));
                response.setUserId((String) result.get("O_USERID"));
            }
            response.serviceResult.setErrorMsg(errMsg);
            response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
        } catch (Exception e) {
            response.serviceResult.setErrorMsg("Exception from createNewUser - UserRepository -" + e.getMessage());
            response.serviceResult.setErrorCode("1");
        }

        return response;
    }

    public UserLoginData handleAdminLogin(UserLoginRequest userRequest) {
        UserLoginData response = new UserLoginData();
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("PKG_USERS")
                    .withProcedureName("PROC_ADMIN_LOGIN");

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("IO_USERID", userRequest.getUserId());

            Map<String, Object> result = jdbcCall.execute(params);
            // System.out.println(result);
            String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
            if (errMsg.isEmpty() || errMsg.equals("")) {
                response.setUserName((String) result.get("O_USERNAME"));
                response.setUserId((String) result.get("IO_USERID"));
                response.setPassword((String) result.get("O_PASSWORD"));
            }
            response.serviceResult.setErrorMsg(errMsg);
            response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
        } catch (Exception e) {
            response.serviceResult.setErrorMsg("Exception from handleUserLogin - UserRepository -" + e.getMessage());
            response.serviceResult.setErrorCode("1");
        }

        return response;
    }

    public User findUserById(String userId) {
        User user = new User();
        try {
            // System.out.println(userId);

            String sql = """
                    SELECT ID, NAME, PASSWORD, ISADMIN
                    FROM USERS
                    WHERE ID = ?
                    """;
            // System.out.println(sql);
            Map<String, Object> result = jdbcTemplate.queryForMap(sql, userId);

            System.out.println("Result" + result);

            user.setUserId((String) result.get("ID"));
            // user.setPassword((String) result.get("NAME"));
            user.setPassword((String) result.get("PASSWORD"));
            user.setUserName((String) result.get("NAME"));
            boolean isAdmin = ((String) result.get("NAME")) == "Y" ? true : false;
            user.setAdmin(isAdmin);
        } catch (DataAccessException ex) {
            // TODO: handle exception
            System.out.println("Spring Exception : " + ex.getClass().getName());
            System.out.println("Message : " + ex.getMessage());

            Throwable cause = ex.getRootCause();
            if (cause != null) {
                System.out.println("Oracle Error : " + cause.getMessage());
            }
        }

        return user;
    }

    public SaveUserAddressResponse handleSaveUserAddress(SaveUserAddressRequest userRequest, String userId) {
        SaveUserAddressResponse response = new SaveUserAddressResponse();
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("PKG_USERS")
                    .withProcedureName("PROC_SAVE_USER_ADDRESS");

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("i_Userid", userId)
                    .addValue("i_Address_Label", userRequest.getAddressLabel())
                    .addValue("i_Addressownername", userRequest.getAddressOwnerName())
                    .addValue("i_Houseno", userRequest.getHouseNo())
                    .addValue("i_Streetname", userRequest.getStreetName())
                    .addValue("i_City", userRequest.getCityOrTown())
                    .addValue("i_District", userRequest.getDistrict())
                    .addValue("i_State", userRequest.getStateName())
                    .addValue("i_Country", userRequest.getCountry())
                    .addValue("i_Pincode", userRequest.getPincode())
                    .addValue("i_Phoneno", userRequest.getPhoneNo());

            Map<String, Object> result = jdbcCall.execute(params);
            // System.out.println(result);
            String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
            // System.out.println(result.get("O_ADDRESS_ID"));
            if (errMsg.isEmpty() || errMsg.equals("")) {
                response.setAddressId((String) result.get("O_ADDRESS_ID"));
                response.serviceResult.setSuccess(true);
            } else {
                response.serviceResult.setSuccess(false);
            }
            response.serviceResult.setErrorMsg(errMsg);
            response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
        } catch (Exception e) {
            response.serviceResult
                    .setErrorMsg("Exception from handleSaveUserAddress - UserRepository -" + e.getMessage());
            response.serviceResult.setSuccess(false);
            response.serviceResult.setErrorCode("1");
        }

        return response;
    }

    public GetUserAddressResponse handleGetAddressList(String userId) {
        GetUserAddressResponse response = new GetUserAddressResponse();

        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName("PKG_USERS")
                    .withProcedureName("PROC_GET_USER_ADDRESS_LIST")
                    .declareParameters(
                            new SqlParameter(
                                    "I_USER_ID",
                                    Types.VARCHAR),
                            new SqlOutParameter(
                                    "O_ADDRESS_CUR",
                                    OracleTypes.CURSOR,

                                    new AddressRowMapper()),
                            new SqlOutParameter(
                                    "O_ERRMSG",
                                    Types.VARCHAR),
                            new SqlOutParameter(
                                    "O_ERRCODE",
                                    Types.VARCHAR));

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("I_USER_ID", userId);

            Map<String, Object> result = new HashMap<String, Object>();
            try {
                // System.out.println("Hello 1");

                result = jdbcCall.execute(params);
                // System.out.println("Hello 3");

            } catch (DataAccessException e) {
                // System.out.println("Hello 2");
                System.out.println(e.getLocalizedMessage());
            }

            String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
            // System.out.println("errMsg = " + errMsg);
            System.out.println(result.toString());
            if (errMsg.isEmpty() || errMsg.equals("")) {
                // System.out.println("Here");
                // System.out.println(result.get("O_PROD_CURSOR"));
                List<Address> addresses = (List<Address>) result.get("O_ADDRESS_CUR");
                response.addressList = addresses;
                response.serviceResult.setSuccess(true);

            } else {
                response.serviceResult.setSuccess(false);
            }

            response.serviceResult.setErrorMsg(errMsg);
            response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
            /// System.out.println(response);

        } catch (Exception e) {
            response.serviceResult
                    .setErrorMsg("Exception from handleGetAddressList - UserRepository -"
                            + e.getMessage());
            response.serviceResult.setErrorCode("1");
            response.serviceResult.setSuccess(false);
        }

        return response;

    }

    public DeleteUserAddressResponse handleDeleteUserAddress(DeleteUserAddressRequest userRequest, String userId) {
        DeleteUserAddressResponse response = new DeleteUserAddressResponse();
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("PKG_USERS")
                    .withProcedureName("PROC_DELETE_USER_ADDRESS");

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("I_USER_ID", userId)
                    .addValue("I_ADDRESS_ID", userRequest.getAddressId());

            Map<String, Object> result = jdbcCall.execute(params);
            // System.out.println(result);
            String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
            // System.out.println(result.get("O_ADDRESS_ID"));
            if (errMsg.isEmpty() || errMsg.equals("")) {
                response.serviceResult.setSuccess(true);
            } else {
                response.serviceResult.setSuccess(false);
            }
            response.serviceResult.setErrorMsg(errMsg);
            response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
        } catch (Exception e) {
            response.serviceResult
                    .setErrorMsg("Exception from handleDeleteUserAddress - UserRepository -" + e.getMessage());
            response.serviceResult.setSuccess(false);
            response.serviceResult.setErrorCode("1");
        }

        return response;
    }

    public void handleUpdateWallet(PlaceOrderRequest request, PlaceOrderResponse response, String userId,
            double totalPayableAmt) {

        try {

            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName("PKG_USERS")
                    .withProcedureName("PROC_UPDATE_WALLET");

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("I_USER_ID", userId)
                    .addValue("I_WALLET_ID", request.walletDetails.getWalletId())
                    .addValue("I_TOTAL_PAYABLE_AMT", totalPayableAmt);

            Map<String, Object> result = jdbcCall.execute(params);
            // System.out.println(result);
            String errMsg = Objects.toString(result.get("O_ERRMSG"), "");
            // System.out.println(result.get("O_ADDRESS_ID"));
            if (errMsg.isEmpty() || errMsg.equals("")) {
                response.serviceResult.setSuccess(true);
            } else {
                response.serviceResult.setSuccess(false);
            }
            response.serviceResult.setErrorMsg(errMsg);
            response.serviceResult.setErrorCode((String) result.get("O_ERRCODE"));
        } catch (Exception e) {
            response.serviceResult
                    .setErrorMsg("Exception from handleUpdateWallet - UserRepository -" + e.getMessage());
            response.serviceResult.setSuccess(false);
            response.serviceResult.setErrorCode("1");
        }

    }
}
