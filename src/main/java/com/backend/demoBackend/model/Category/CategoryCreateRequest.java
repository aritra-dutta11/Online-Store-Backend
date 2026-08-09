package com.backend.demoBackend.model.Category;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CategoryCreateRequest {
    private String categoryName;
    private String categoryDesc;
}
