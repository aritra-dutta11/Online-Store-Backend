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
public class GetUserWalletResponse {
    private String walletId;
    private double amount;
    public ServiceResult serviceResult = new ServiceResult();
}
