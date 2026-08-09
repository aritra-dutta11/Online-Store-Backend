package com.backend.demoBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.demoBackend.model.Category.CategoryCreateRequest;
import com.backend.demoBackend.model.Category.CategoryCreateResponse;
import com.backend.demoBackend.model.Category.GetCategoryResponse;
import com.backend.demoBackend.service.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryCreateResponse> createCategory(
            @RequestPart("categoryReq") CategoryCreateRequest catRequest,
            @RequestPart("categoryImage") MultipartFile categoryImage) {

        CategoryCreateResponse catRes = categoryService.createCategory(catRequest, categoryImage);
        // System.out.println(categoryImage.getOriginalFilename());
        // CategoryCreateResponse catRes = new CategoryCreateResponse();

        if ("".equals(catRes.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(catRes);
        }
        return ResponseEntity.ok().body(catRes);
    }

    @GetMapping("/getAll")
    public ResponseEntity<GetCategoryResponse> getAllCategories() {
        GetCategoryResponse catRes = categoryService.getCategoriesList();

        if ("".equals(catRes.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(catRes);
        }
        return ResponseEntity.ok().body(catRes);
    }

}
