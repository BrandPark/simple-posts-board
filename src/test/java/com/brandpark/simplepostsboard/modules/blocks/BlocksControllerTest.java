package com.brandpark.simplepostsboard.modules.blocks;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
public class BlocksControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountFactory accountFactory;
    Accounts user;

    @BeforeEach
    public void setUp() {
        user = accountFactory.createAndPersistAccount("user", "1q2w3e4r");
    }

    @DisplayName("차단 리스트 페이지 로딩 - 실패(로그인 하지 않은 경우)")
    @Test
    public void BlockedListView_Fail_Unauthenticated() throws Exception {

        mockMvc.perform(get("/accounts/" + user.getId() + "/blocks"))
                .andExpect(status().is3xxRedirection());
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("차단 리스트 페이지 로딩 - 성공")
    @Test
    public void BlockedListView_Success() throws Exception {

        mockMvc.perform(get("/accounts/" + user.getId() + "/blocks"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("list-blocked"));
    }
}
