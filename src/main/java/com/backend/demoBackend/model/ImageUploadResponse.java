package com.backend.demoBackend.model;

import org.springframework.stereotype.Component;

import com.backend.demoBackend.model.Service.ServiceResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ImageUploadResponse {

    public PicrdResponse picrdResponse = new PicrdResponse();
    public ServiceResult serviceResult = new ServiceResult();

}
