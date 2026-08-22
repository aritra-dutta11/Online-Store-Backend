package com.backend.demoBackend.model.Cart;

import org.springframework.stereotype.Component;

import com.backend.demoBackend.model.Service.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class DeleteCartResponse {
    private String cartId;
    public ServiceResult serviceResult = new ServiceResult();
}
