package com.ecommerce.project.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResourceNotFoundException  extends  RuntimeException{
    String fieldName;
    String field;
    Long fieldId;
    String resourceName;

    public ResourceNotFoundException(String fieldName, String field, String resourceName) {
        this.fieldName = fieldName;
        this.field = field;
        this.resourceName = resourceName;
    }

    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
        super(String.format("%s could not found  %s %d", resourceName,field,fieldId));
        this.resourceName = resourceName;
        this.fieldId = fieldId;
        this.field = field;
    }
}
