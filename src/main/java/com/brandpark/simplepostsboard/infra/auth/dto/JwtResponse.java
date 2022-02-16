package com.brandpark.simplepostsboard.infra.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {
    private String jwtToken;
}
