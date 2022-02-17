package com.brandpark.simplepostsboard.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionResponse {
    private int statusCode;
    private String statusName;
    private String message;
}
