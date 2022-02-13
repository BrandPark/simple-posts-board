package com.brandpark.simplepostsboard.modules.accounts;

import com.brandpark.simplepostsboard.AccountFactory;
import com.brandpark.simplepostsboard.MockMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class AccountControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountRepository accountRepository;
    @Autowired AccountFactory accountFactory;

    @DisplayName("회원가입 페이지 로딩")
    @Test
    public void SignUpView() throws Exception {

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

        // when
        mockMvc.perform(post("/accounts/sign-up")
                        .param("nickname", invalidNickname)
                        .param("password", validPassword)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("sign-up"))
                .andExpect(unauthenticated());

        // then
        assertThat(accountRepository.findAll().size()).isEqualTo(0);
    }

    @DisplayName("회원가입 - 실패(이미 존재하는 닉네임)")
    @Test
    public void SignUpSubmit_Fail_When_DuplicatedNickname() throws Exception {

        // given
        String duplicatedNickname = "duplicated";
        String validPassword = "1q2w3e4r";

        accountFactory.createAndPersistAccount(duplicatedNickname, validPassword);

        // when
        mockMvc.perform(post("/accounts/sign-up")
                        .param("nickname", duplicatedNickname)
                        .param("password", validPassword)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("sign-up"))
                .andExpect(unauthenticated());

        // then
        assertThat(accountRepository.findAll().size()).isEqualTo(1);
    }


    @DisplayName("회원가입 - 성공")
    @Test
    public void SignUpSubmit_Success() throws Exception {

        // given
        String validNickname = "validNickname";
        String validPassword = "1q2w3e4r";

        // when
        mockMvc.perform(post("/accounts/sign-up")
                        .param("nickname", validNickname)
                        .param("password", validPassword)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated().withUsername("validNickname"));

        // then
        assertThat(accountRepository.findAll().size()).isEqualTo(1);
    }
}