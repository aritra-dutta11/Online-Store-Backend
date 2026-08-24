package com.backend.demoBackend.model.User;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Address {
    private String houseNo;
    private String streetName;
    private String cityOrTown;
    private String district;
    private String country;
    private String pincode;
    private String phoneNo;
    private String stateName;
    private String addressLabel;
    private String addressOwnerName;
    private String addressId;
}
