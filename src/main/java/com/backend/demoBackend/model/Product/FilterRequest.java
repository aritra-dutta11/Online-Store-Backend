package com.backend.demoBackend.model.Product;

import org.springframework.stereotype.Component;

import com.backend.demoBackend.model.Service.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class FilterRequest {
    private double minPrice;
    private double maxPrice;
    private String categoryId;
    private double rating;
}
