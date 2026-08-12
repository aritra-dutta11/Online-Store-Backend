package com.backend.demoBackend.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.demoBackend.model.Product.CreateProductRequest;
import com.backend.demoBackend.model.Product.CreateProductResponse;
import com.backend.demoBackend.model.Product.FilterRequest;
import com.backend.demoBackend.model.Product.GetProductResponse;
import com.backend.demoBackend.model.Product.GetSingleProductResponse;
import com.backend.demoBackend.model.Product.Product;
import com.backend.demoBackend.model.Product.ProductImageUploadResponse;
import com.backend.demoBackend.model.Product.ProductReviewRequest;
import com.backend.demoBackend.model.Product.ProductReviewResponse;
import com.backend.demoBackend.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository prodRepo;

    @Autowired
    ImageUploadService imageUploadService;

    // List<Product> products = new ArrayList<>(
    // Arrays.asList(new Product(1, "Pixel 10", 60000), new Product(2, "HP Victus",
    // 80000)));

    // public List<Product> getProducts() {
    // return products;
    // }

    // public Product getProductById(int prodId) {
    // return products.stream().filter(p -> p.getProductId() ==
    // prodId).findFirst().get();
    // }

    // public List<Product> addProduct(Product prod) {
    // products.add(prod);
    // return products;
    // }

    // public List<Product> updateProduct(Product prod) {
    // for (Product p : products) {
    // if (p.getProductId() == prod.getProductId()) {
    // p.setPrice(prod.getPrice());
    // p.setProdName(prod.getProdName());
    // }
    // }
    // return products;
    // }

    // public List<Product> deleteProduct(int prodId) {
    // products.removeIf((p -> prodId == p.getProductId()));
    // return products;
    // }

    public CreateProductResponse createNewProduct(CreateProductRequest prodRequest, MultipartFile[] productImages) {
        CreateProductResponse prodRes = new CreateProductResponse();
        // System.out.println(prodRequest.toString());
        try {
            ProductImageUploadResponse prodImgRes = imageUploadService.uploadProductsImage(productImages);
            System.out.println(prodImgRes.toString());
            if ("".equals(prodImgRes.serviceResult.getErrorMsg())) {
                prodRes = prodRepo.createNewProduct(prodRequest, prodImgRes.picrdResponseList);
            } else {
                prodRes.serviceResult
                        .setErrorMsg(prodImgRes.serviceResult.getErrorMsg());
                prodRes.serviceResult.setErrorCode(prodImgRes.serviceResult.getErrorCode());
            }

        } catch (Exception e) {
            prodRes.serviceResult.setErrorMsg("Exception from createNewProduct - ProductService : " + e.getMessage());
            prodRes.serviceResult.setErrorCode("1");
        }
        return prodRes;
    }

    public GetProductResponse getProducts(int pageNo) {
        GetProductResponse prodRes = new GetProductResponse();
        try {
            prodRes = prodRepo.handleGetProducts(pageNo);
        } catch (Exception e) {
            prodRes.serviceResult.setErrorMsg("Exception from getProducts - ProductService : " + e.getMessage());
            prodRes.serviceResult.setErrorCode("1");
        }
        return prodRes;
    }

    public GetProductResponse getProducts(String searchKey, int pageNo, FilterRequest filters) {
        GetProductResponse prodRes = new GetProductResponse();
        try {
            prodRes = prodRepo.handleGetProducts(searchKey, pageNo, filters);
        } catch (Exception e) {
            prodRes.serviceResult.setErrorMsg("Exception from getProducts - ProductService : " + e.getMessage());
            prodRes.serviceResult.setErrorCode("1");
        }
        return prodRes;
    }

    public ProductReviewResponse addReviews(ProductReviewRequest prodReq, String userId) {
        ProductReviewResponse prodRes = new ProductReviewResponse();
        try {
            prodRes = prodRepo.handleAddNewReview(prodReq, userId);
        } catch (Exception e) {
            prodRes.serviceResult.setErrorMsg("Exception from getProducts - ProductService : " + e.getMessage());
            prodRes.serviceResult.setErrorCode("1");
        }
        return prodRes;
    }

    public GetSingleProductResponse getSingleProduct(String prodId) {
        GetSingleProductResponse prodRes = new GetSingleProductResponse();
        try {
            prodRes = prodRepo.handleGetSingleProduct(prodId);
        } catch (Exception e) {
            prodRes.serviceResult.setErrorMsg("Exception from getProducts - ProductService : " + e.getMessage());
            prodRes.serviceResult.setErrorCode("1");
        }
        return prodRes;
    }
}
