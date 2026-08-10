package com.backend.demoBackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.demoBackend.model.ImageUploadResponse;
import com.backend.demoBackend.model.PicrdResponse;
import com.backend.demoBackend.model.Service.ServiceResult;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class ImageUploadService {

    @Value("${picrd.base_url}")
    private String picrdBaseURL;
    private final RestClient restClient = RestClient.create();

    public ImageUploadResponse uploadImage(MultipartFile image) throws Exception {
        ImageUploadResponse response = new ImageUploadResponse();
        try {
            ByteArrayResource resource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            body.add("file", resource);
            body.add("visibility", "public");
            String apiUrl = picrdBaseURL + "/api/upload";
            // System.out.println(apiUrl);
            response.picrdResponse = restClient.post()
                    .uri(apiUrl)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(PicrdResponse.class);
            response.serviceResult.setErrorMsg("");
            response.serviceResult.setErrorCode("");
        } catch (Exception e) {
            response.serviceResult.setErrorMsg("Exception while uploading image - " + e.getMessage());
            response.serviceResult.setErrorCode("1");
        }

        return response;
    };
}
