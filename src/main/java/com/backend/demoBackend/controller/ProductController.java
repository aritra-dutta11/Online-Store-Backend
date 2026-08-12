package com.backend.demoBackend.controller;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.demoBackend.model.Product.CreateProductRequest;
import com.backend.demoBackend.model.Product.CreateProductResponse;
import com.backend.demoBackend.model.Product.FilterRequest;
import com.backend.demoBackend.model.Product.GetProductResponse;
import com.backend.demoBackend.model.Product.GetSingleProductResponse;
import com.backend.demoBackend.model.Product.ProductReviewRequest;
import com.backend.demoBackend.model.Product.ProductReviewResponse;
import com.backend.demoBackend.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    // @GetMapping("/products")
    // public List<Product> getProducts() {
    // return productService.getProducts();
    // }

    // @GetMapping("/products/{prodId}")
    // public Product getProductsByID(@PathVariable int prodId) {
    // return productService.getProductById(prodId);
    // }

    // @PostMapping("/add/product")
    // public List<Product> getProductsByID(@RequestBody Product prod) {
    // return productService.addProduct(prod);
    // }

    // @PutMapping("/update/product")
    // public List<Product> updateProductById(@RequestBody Product prod) {
    // return productService.updateProduct(prod);
    // }

    // @DeleteMapping("/delete/product/{prodId}")
    // public List<Product> deleteProductById(@PathVariable int prodId) {
    // return productService.deleteProduct(prodId);
    // }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateProductResponse> addNewProduct(
            @RequestPart("productReq") CreateProductRequest prodRequest,
            @RequestPart("productImages") MultipartFile[] productImages) {
        // System.out.println(productImages.length);
        CreateProductResponse prodRes = productService.createNewProduct(prodRequest, productImages);
        // CreateProductResponse prodRes = new CreateProductResponse();

        if ("".equals(prodRes.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(prodRes);
        }
        return ResponseEntity.ok().body(prodRes);
    }

    @GetMapping("/getProducts/{pageNo}")
    public ResponseEntity<GetProductResponse> getAddProducts(@PathVariable int pageNo) {
        // System.out.println(prodRequest.toString());
        // System.out.println(pageNo);
        GetProductResponse prodRes = productService.getProducts(pageNo);

        if ("".equals(prodRes.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(prodRes);
        }
        return ResponseEntity.ok().body(prodRes);
    }

    @GetMapping("/getProducts/{searchKey}/{pageNo}")
    public ResponseEntity<GetProductResponse> getSearchedProducts(@PathVariable int pageNo,
            @PathVariable String searchKey, @RequestBody FilterRequest filters) {
        // System.out.println(prodRequest.toString());
        System.out.println(pageNo);
        System.out.println(searchKey);
        GetProductResponse prodRes = productService.getProducts(searchKey, pageNo, filters);

        if ("".equals(prodRes.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(prodRes);
        }
        return ResponseEntity.ok().body(prodRes);
    }

    @PostMapping("/reviews/add")
    public ResponseEntity<ProductReviewResponse> addReviews(Authentication auth,
            @RequestBody ProductReviewRequest prodReviewReq) {

        ProductReviewResponse prodRes = productService.addReviews(prodReviewReq, auth.getName());

        if ("".equals(prodRes.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(prodRes);
        }
        return ResponseEntity.ok().body(prodRes);
    }

    @GetMapping("/getProduct/{prodId}")
    public ResponseEntity<GetSingleProductResponse> getProduct(@PathVariable String prodId) {

        GetSingleProductResponse prodRes = productService.getSingleProduct(prodId);

        if ("".equals(prodRes.serviceResult.getErrorMsg())) {
            return ResponseEntity.ok(prodRes);
        }
        return ResponseEntity.ok().body(prodRes);
    }

}
