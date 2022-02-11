package com.brandpark.simplepostsboard.modules;

import com.brandpark.simplepostsboard.AccountFactory;
import com.brandpark.simplepostsboard.MockMvcTest;
import com.brandpark.simplepostsboard.api.Accounts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class HomeControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountFactory accountFactory;
    Accounts savedAccounts;

    @BeforeEach
    public void setUp() {
        savedAccounts = accountFactory.createAndPersistAccount("user", "password");
    }

    @DisplayName("메인 페이지 로딩 - 로그인 안 했을 때")
    @Test
    public void MainPage_When_NotAuthenticated() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("account"))
                .andExpect(view().name("home"));
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("메인 페이지 로딩 - 로그인 했을 때")
    @Test
    public void MainPage_When_Authenticated() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(view().name("home"));
    }
}