package com.backend.demoBackend.model.Product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Product {

    private String productId;
    private String prodName;
    private int price;
    private int quantity;
    private String prodDesc;
    private String category;
    private double avgRating;
    public List<ProductReview> reviews = new ArrayList<>();
}
