package com.brandpark.simplepostsboard.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public ApiSubError(String object, String message) {
        this.object = object;
        this.message = message;
    }

    public ApiSubError(FieldError fieldError) {
        object = fieldError.getObjectName();
        field = fieldError.getField();
        rejectedValue = fieldError.getRejectedValue();
        message = fieldError.getDefaultMessage();
    }
}
