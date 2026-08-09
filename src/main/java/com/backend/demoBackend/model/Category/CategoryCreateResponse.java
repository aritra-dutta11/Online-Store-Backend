package com.backend.demoBackend.model.Category;

import org.springframework.stereotype.Component;

import com.backend.demoBackend.model.Service.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CategoryCreateResponse {
    private String categoryName;
    private String categoryId;
    public ServiceResult serviceResult = new ServiceResult();
}
