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
public class ProductReviewResponse {

    private String productId;
    private String userId;
    private String comment;
    private double rating;
    private String reviewId;
    public ServiceResult serviceResult = new ServiceResult();

}
