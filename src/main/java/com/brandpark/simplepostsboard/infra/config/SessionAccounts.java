package com.brandpark.simplepostsboard.infra.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SessionAccounts implements Serializable {

    private Long id;

    private String nickname;

    private String password;
}
