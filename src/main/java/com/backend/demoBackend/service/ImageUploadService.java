package com.backend.demoBackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public PicrdResponse uploadImage(MultipartFile image) throws Exception {
        PicrdResponse response = new PicrdResponse();

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
        System.out.println(apiUrl);
        response = restClient.post()
                .uri(apiUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(PicrdResponse.class);
        // response.serviceResult = new ServiceResult();
        // response.serviceResult.setErrorCode("");

        return response;
    };
}
