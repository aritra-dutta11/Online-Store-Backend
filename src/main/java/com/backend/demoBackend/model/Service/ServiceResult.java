package com.backend.demoBackend.model.Service;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ServiceResult {
    private String errorMsg = "";
    private String errorCode = "";
    private boolean success = true;
}
