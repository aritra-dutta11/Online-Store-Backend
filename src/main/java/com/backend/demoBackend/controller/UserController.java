package com.backend.demoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.demoBackend.model.User.GetUserWalletResponse;
import com.backend.demoBackend.model.User.SaveUserAddressRequest;
import com.backend.demoBackend.model.User.SaveUserAddressResponse;
import com.backend.demoBackend.model.User.UserLoginRequest;
import com.backend.demoBackend.model.User.UserLoginResponse;
import com.backend.demoBackend.model.User.UserRequest;
import com.backend.demoBackend.model.User.UserResponse;
import com.backend.demoBackend.service.UserService;

@RestController
@RequestMapping(("/api/users"))
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest userInput) {
        UserResponse userResponse = userService.createNewUser(userInput);

        if ("".equals(userResponse.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(userResponse);
        }
        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody UserLoginRequest userInput) {
        System.out.println(userInput.toString());
        UserLoginResponse userResponse = userService.userLogin(userInput);

        if ("".equals(userResponse.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(userResponse);
        }
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/wallet/get")
    public ResponseEntity<GetUserWalletResponse> getWallet(Authentication auth) {
        // System.out.println(userInput.toString());
        GetUserWalletResponse userResponse = userService.getUserWallet(auth.getName());

        if ("".equals(userResponse.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(userResponse);
        }
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/address/save")
    public ResponseEntity<SaveUserAddressResponse> saveUserAddress(@RequestBody SaveUserAddressRequest req,
            Authentication auth) {
        // System.out.println(userInput.toString());
        System.out.println(req.toString());
        SaveUserAddressResponse userResponse = userService.saveUserAddress(req, auth.getName());

        if ("".equals(userResponse.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(userResponse);
        }
        return ResponseEntity.ok(userResponse);
    }

}
