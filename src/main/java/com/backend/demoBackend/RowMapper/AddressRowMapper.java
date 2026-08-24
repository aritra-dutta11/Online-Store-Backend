package com.backend.demoBackend.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.backend.demoBackend.model.User.Address;

public class AddressRowMapper implements RowMapper<Address> {
    @Override
    public Address mapRow(ResultSet rs, int rowNum)
            throws SQLException {
        Address address = new Address();
        address.setAddressId(rs.getString("ADDRESS_ID"));
        address.setAddressLabel(rs.getString("ADDRESS_LABEL"));
        address.setHouseNo(rs.getString("HOUSE_NO"));
        address.setStreetName(rs.getString("STREET_NAME"));
        address.setCityOrTown(rs.getString("CITY_NAME"));
        address.setDistrict(rs.getString("DISTRICT_NAME"));
        address.setCountry(rs.getString("COUNTRY_NAME"));
        address.setStateName(rs.getString("STATE_NAME"));
        address.setAddressOwnerName(rs.getString("USER_NAME"));
        address.setPhoneNo(rs.getString("PHONE_NO"));

        return address;
    }
}
