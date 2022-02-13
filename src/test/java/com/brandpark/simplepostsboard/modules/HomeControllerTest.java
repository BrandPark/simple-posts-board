package com.brandpark.simplepostsboard.modules;

import com.brandpark.simplepostsboard.AccountFactory;
import com.brandpark.simplepostsboard.MockMvcTest;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
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
    public void MainView_When_NotAuthenticated() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("account"))
                .andExpect(view().name("home"));
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("메인 페이지 로딩 - 로그인 했을 때")
    @Test
    public void MainView_When_Authenticated() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(view().name("home"));
    }

    @DisplayName("로그인 - 성공")
    @Test
    public void Login_Success() throws Exception {

        // given
        String username = "username";
        String password = "password";
        Accounts newAccount = accountFactory.createAndPersistAccount(username, password);

        // when, then
        mockMvc.perform(post("/login")
                        .param("username", username)
                        .param("password", password)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated().withUsername("username"));
    }

    @DisplayName("로그인 - 실패(비밀번호 오류)")
    @Test
    public void Login_Success_When_WrongInputPassword() throws Exception {

        // given
        String username = "username";
        String password = "password";
        Accounts newAccount = accountFactory.createAndPersistAccount(username, password);

        String wrongPassword = "password123";

        // when, then
        mockMvc.perform(post("/login")
                        .param("username", username)
                        .param("password", wrongPassword)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(unauthenticated());
    }
}