package com.backend.demoBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.demoBackend.model.JWTModel;
import com.backend.demoBackend.model.User.GetUserWalletResponse;
import com.backend.demoBackend.model.User.SaveUserAddressRequest;
import com.backend.demoBackend.model.User.SaveUserAddressResponse;
import com.backend.demoBackend.model.User.UserLoginData;
import com.backend.demoBackend.model.User.UserLoginRequest;
import com.backend.demoBackend.model.User.UserLoginResponse;
import com.backend.demoBackend.model.User.UserRequest;
import com.backend.demoBackend.model.User.UserResponse;
import com.backend.demoBackend.repository.UserRepository;;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserResponse createNewUser(UserRequest userInput) {
        UserResponse userOutput = new UserResponse();
        try {
            userInput.setPassword(encoder.encode(userInput.getPassword()));
            userOutput = userRepo.createNewUser(userInput);

            if ("".equals(userOutput.serviceResult.getErrorMsg())) {
                if (userOutput.getUserId() != null && "".equals(userOutput.getUserId()))
                    userRepo.createUserWallet(userOutput);
            }
        } catch (Exception e) {
            userOutput.serviceResult.setErrorMsg("Exception from createNewUser - UserService " + e.getMessage());
            userOutput.serviceResult.setErrorCode("1");
        }
        return userOutput;
    }

    public UserLoginResponse userLogin(UserLoginRequest userInput) {
        UserLoginData userDataOutput = new UserLoginData();
        UserLoginResponse userOutput = new UserLoginResponse();
        try {

            userDataOutput = userRepo.handleUserLogin(userInput);
            // System.out.println(userDataOutput.toString());
            // System.out.println(userDataOutput.serviceResult.getErrorMsg());
            if ("".equals(userDataOutput.serviceResult.getErrorMsg())) {
                // Check for correct password
                boolean isValidPassword = encoder.matches(userInput.getPassword(), userDataOutput.getPassword());
                // System.out.println(isValidPassword);

                if (isValidPassword) {

                    JWTModel jwtModel = new JWTModel();
                    jwtModel.setUserId(userDataOutput.getUserId());
                    String token = jwtService.generateToken(jwtModel);

                    userOutput.setToken(token);
                    userOutput.serviceResult.setErrorMsg("");
                    userOutput.serviceResult.setErrorCode("0");
                    userOutput.setUserId(userDataOutput.getUserId());
                    userOutput.setUserName(userDataOutput.getUserName());
                    userOutput.setAdmin(false);
                } else {
                    userOutput.serviceResult.setErrorMsg("Invalid Password!");
                    userOutput.serviceResult.setErrorCode("1");
                }
            } else {
                userOutput.serviceResult.setErrorMsg(userDataOutput.serviceResult.getErrorMsg());
                userOutput.serviceResult.setErrorCode(userDataOutput.serviceResult.getErrorCode());
            }
        } catch (Exception e) {
            userOutput.serviceResult.setErrorMsg("Exception from userLogin - UserService " + e.getMessage());
            userOutput.serviceResult.setErrorCode("1");
        }
        return userOutput;
    }

    public UserResponse createNewAdmin(UserRequest userInput) {
        UserResponse userOutput = new UserResponse();
        try {
            userInput.setPassword(encoder.encode(userInput.getPassword()));
            userOutput = userRepo.createNewAdmin(userInput);
        } catch (Exception e) {
            userOutput.serviceResult.setErrorMsg("Exception from createNewAdmin - UserService " + e.getMessage());
            userOutput.serviceResult.setErrorCode("1");
        }
        return userOutput;
    }

    public UserLoginResponse adminLogin(UserLoginRequest userInput) {
        UserLoginData userDataOutput = new UserLoginData();
        UserLoginResponse userOutput = new UserLoginResponse();
        try {

            userDataOutput = userRepo.handleAdminLogin(userInput);
            // System.out.println(userDataOutput.toString());
            // System.out.println(userDataOutput.serviceResult.getErrorMsg());
            if ("".equals(userDataOutput.serviceResult.getErrorMsg())) {
                // Check for correct password
                boolean isValidPassword = encoder.matches(userInput.getPassword(), userDataOutput.getPassword());
                // System.out.println(isValidPassword);

                if (isValidPassword) {

                    JWTModel jwtModel = new JWTModel();
                    jwtModel.setUserId(userDataOutput.getUserId());
                    String token = jwtService.generateToken(jwtModel);

                    userOutput.setToken(token);
                    userOutput.serviceResult.setErrorMsg("");
                    userOutput.serviceResult.setErrorCode("0");
                    userOutput.setUserId(userDataOutput.getUserId());
                    userOutput.setUserName(userDataOutput.getUserName());
                    userOutput.setAdmin(true);
                } else {
                    userOutput.serviceResult.setErrorMsg("Invalid Password!");
                    userOutput.serviceResult.setErrorCode("1");
                }
            } else {
                userOutput.serviceResult.setErrorMsg(userDataOutput.serviceResult.getErrorMsg());
                userOutput.serviceResult.setErrorCode(userDataOutput.serviceResult.getErrorCode());
            }
        } catch (Exception e) {
            userOutput.serviceResult.setErrorMsg("Exception from userLogin - UserService " + e.getMessage());
            userOutput.serviceResult.setErrorCode("1");
        }
        return userOutput;
    }

    public GetUserWalletResponse getUserWallet(String userId) {
        GetUserWalletResponse response = new GetUserWalletResponse();
        try {
            response = userRepo.handleGetWallet(userId);
        } catch (Exception e) {
            response.serviceResult.setErrorMsg("Exception from getUserWallet - UserService " + e.getMessage());
            response.serviceResult.setErrorCode("1");
            response.serviceResult.setSuccess(false);
        }
        return response;
    }

    public SaveUserAddressResponse saveUserAddress(SaveUserAddressRequest req, String userId) {
        SaveUserAddressResponse response = new SaveUserAddressResponse();
        String strValidationMsg = "";
        try {
            if ("".equals(req.getAddressLabel())) {
                strValidationMsg += "Address Label cannot be blank!";
            }

            if ("".equals(req.getAddressOwnerName())) {
                strValidationMsg += "Address Owner Name cannot be blank!";
            }

            if ("".equals(req.getHouseNo())) {
                strValidationMsg += "House/Apartment No. cannot be blank!";
            }

            if ("".equals(req.getStreetName())) {
                strValidationMsg += "Street Name cannot be blank!";
            }

            if ("".equals(req.getCityOrTown())) {
                strValidationMsg += "City/Town cannot be blank!";
            }

            if ("".equals(req.getStateName())) {
                strValidationMsg += "State cannot be blank!";
            }

            if ("".equals(req.getPincode())) {
                strValidationMsg += "Pincode cannot be blank!";
            }

            if ("".equals(req.getPhoneNo())) {
                strValidationMsg += "Phone No cannot be blank!";
            }

            if ("".equals(strValidationMsg)) {
                response = userRepo.handleSaveUserAddress(req, userId);
            } else {
                response.serviceResult.setErrorMsg(strValidationMsg);
                response.serviceResult.setErrorCode("1");
                response.serviceResult.setSuccess(false);
            }

        } catch (Exception e) {
            response.serviceResult.setErrorMsg("Exception from saveUserAddress - UserService " + e.getMessage());
            response.serviceResult.setErrorCode("1");
            response.serviceResult.setSuccess(false);
        }
        return response;
    }
}
