package com.backend.demoBackend.model.Product;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ProductReviewRequest {

    private String productId;
    private String comment;
    private double rating;

}
