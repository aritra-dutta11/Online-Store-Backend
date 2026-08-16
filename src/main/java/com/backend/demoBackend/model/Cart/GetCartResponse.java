package com.backend.demoBackend.model.Cart;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.backend.demoBackend.model.Service.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class GetCartResponse {
    public List<CartProduct> cartProductList = new ArrayList<>();
    private String cartId;
    public ServiceResult serviceResult = new ServiceResult();
}
