package com.backend.demoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.demoBackend.model.User.UserLoginRequest;
import com.backend.demoBackend.model.User.UserLoginResponse;
import com.backend.demoBackend.model.User.UserRequest;
import com.backend.demoBackend.model.User.UserResponse;
import com.backend.demoBackend.service.UserService;

@RestController
@RequestMapping(("/api/admin"))
public class AdminController {

    @Autowired
    UserService userService;

    @PostMapping("/add")
    public ResponseEntity<UserResponse> addAdmin(@RequestBody UserRequest userInput) {
        UserResponse userResponse = userService.createNewAdmin(userInput);

        if ("".equals(userResponse.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(userResponse);
        }
        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginAdmin(@RequestBody UserLoginRequest userInput) {
        UserLoginResponse userResponse = userService.adminLogin(userInput);

        if ("".equals(userResponse.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(userResponse);
        }
        return ResponseEntity.ok().body(userResponse);
    }

}
