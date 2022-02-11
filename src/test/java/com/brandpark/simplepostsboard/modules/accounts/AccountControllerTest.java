package com.brandpark.simplepostsboard.modules.accounts;

import com.brandpark.simplepostsboard.MockMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class AccountControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountRepository accountRepository;

    @DisplayName("회원가입 페이지 로딩")
    @Test
    public void SignUpPage() throws Exception {

        mockMvc.perform(get("/accounts/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }

    @DisplayName("회원가입 - 실패(입력값 오류 : 문자가 아닌 닉네임)")
    @Test
    public void SignUpSubmit_Fail_When_InvalidNickname() throws Exception {

        // given
        String invalidNickname = "_-@#$";
        String validPassword = "1q2w3e4r";

        // when, then
        mockMvc.perform(post("/accounts/sign-up")
                        .param("nickname", invalidNickname)
                        .param("password", validPassword)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원가입 - 성공")
    @Test
    public void SignUpSubmit_Success() throws Exception {

        // given
        String validNickname = "validNickname";
        String validPassword = "1q2w3e4r";

        // when, then
        mockMvc.perform(post("/accounts/sign-up")
                        .param("nickname", validNickname)
                        .param("password", validPassword)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated().withUsername("validNickname"));
    }
}