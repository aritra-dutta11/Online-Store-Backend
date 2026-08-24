package com.backend.demoBackend.model.User;

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
public class GetUserAddressResponse {
    public List<Address> addressList = new ArrayList<>();
    public ServiceResult serviceResult = new ServiceResult();

}
