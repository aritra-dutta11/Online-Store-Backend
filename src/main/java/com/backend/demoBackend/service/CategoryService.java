package com.backend.demoBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.demoBackend.model.ImageUploadResponse;
import com.backend.demoBackend.model.PicrdResponse;
import com.backend.demoBackend.model.Category.CategoryCreateRequest;
import com.backend.demoBackend.model.Category.CategoryCreateResponse;
import com.backend.demoBackend.model.Category.GetCategoryResponse;
import com.backend.demoBackend.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository catRepo;

    @Autowired
    ImageUploadService imageUploadService;

    public CategoryCreateResponse createCategory(CategoryCreateRequest categoryReq, MultipartFile categoryImage) {
        CategoryCreateResponse catRes = new CategoryCreateResponse();
        try {
            ImageUploadResponse imageUploadRes = imageUploadService.uploadImage(categoryImage);
            System.out.println(imageUploadRes.toString());
            if ("".equals(imageUploadRes.serviceResult.getErrorMsg())) {
                catRes = catRepo.handleCreateCategory(categoryReq, imageUploadRes);
            }

        } catch (Exception e) {
            catRes.serviceResult.setErrorMsg("Exception from createCategory - CategoryService : " + e.getMessage());
            catRes.serviceResult.setErrorCode("1");
        }

        return catRes;
    }

    public GetCategoryResponse getCategoriesList() {
        GetCategoryResponse catRes = new GetCategoryResponse();
        try {
            catRes = catRepo.handleGetCategories();
        } catch (Exception e) {
            catRes.serviceResult.setErrorMsg("Exception from createCategory - CategoryService : " + e.getMessage());
            catRes.serviceResult.setErrorCode("1");
        }

        return catRes;
    }
}
