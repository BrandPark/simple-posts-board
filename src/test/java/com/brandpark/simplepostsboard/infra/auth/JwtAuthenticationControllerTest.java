package com.brandpark.simplepostsboard.infra.auth;

import com.brandpark.simplepostsboard.AccountFactory;
import com.brandpark.simplepostsboard.MockMvcTest;
import com.brandpark.simplepostsboard.infra.auth.jwt.dto.JwtRequest;
import com.brandpark.simplepostsboard.modules.accounts.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class JwtAuthenticationControllerTest {

    @Autowired AccountFactory accountFactory;
    @Autowired AccountRepository accountRepository;
    @Autowired ObjectMapper objectMapper;
    @Autowired MockMvc mockMvc;

    @DisplayName("JWT 발급 - 성공")
    @Test
    public void getJWT_Success() throws Exception {

        // given
        accountFactory.createAndPersistAccount("user", "1q2w3e4r");

        JwtRequest reqData = new JwtRequest("user", "1q2w3e4r");

        boolean isUnauthenticated = SecurityContextHolder.getContext().getAuthentication() == null;
        assertThat(isUnauthenticated).isTrue();

        // when
        mockMvc.perform(post("/authenticate")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqData)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    System.out.println(json);
                });

        // then

    }

}