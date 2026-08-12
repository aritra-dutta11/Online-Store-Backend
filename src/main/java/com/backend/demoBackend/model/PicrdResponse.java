package com.backend.demoBackend.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PicrdResponse {

    private String image_id;
    private String page_url;
    private String image_url;
    private String delete_url;
    private String expires_at;
}
