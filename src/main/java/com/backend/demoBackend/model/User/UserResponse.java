package com.backend.demoBackend.model.User;

import org.springframework.stereotype.Component;

import com.backend.demoBackend.model.Service.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserResponse {
    private String userId;
    private String userName;
    public ServiceResult serviceResult = new ServiceResult();
}
