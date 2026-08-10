package com.backend.demoBackend.model.Product;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CreateProductRequest {
    private String productName;
    private String productBrand;
    private String categoryId;
    private String productDesc;
    private int price;
    private int quantity;
}
