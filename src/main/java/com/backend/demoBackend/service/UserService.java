package com.backend.demoBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.demoBackend.model.JWTModel;
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
}
