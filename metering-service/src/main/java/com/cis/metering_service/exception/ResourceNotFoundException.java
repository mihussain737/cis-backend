package com.cis.metering_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private String resourceId;
    private String resourceValue;
    public ResourceNotFoundException(String resourceName, String resourceId,String resourceValue) {
        super(String.format("%s not found with %s: %s", resourceName, resourceId,resourceValue));
        this.resourceName = resourceName;
        this.resourceId = resourceId;
        this.resourceValue = resourceValue;
    }
}
