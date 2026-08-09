package com.backend.demoBackend.model.User;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class User {
    private String userId;
    private String userName;
    private boolean isAdmin;
    private String password;
}
