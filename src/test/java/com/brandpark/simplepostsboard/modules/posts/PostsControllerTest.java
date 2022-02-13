package com.brandpark.simplepostsboard.modules.posts;

import com.brandpark.simplepostsboard.AccountFactory;
import com.brandpark.simplepostsboard.MockMvcTest;
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
class PostsControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountFactory accountFactory;

    @BeforeEach
    public void setUp() {
        accountFactory.createAndPersistAccount("user", "1q2w3e4r");
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("글 쓰기 페이지 로딩 - 성공(로그인 했을 경우)")
    @Test
    public void CreatePostsView_Success() throws Exception {

        mockMvc.perform(get("/posts/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-posts"));
    }

    @DisplayName("글 쓰기 페이지 로딩 - 실패(로그인 하지 않았을 경우)")
    @Test
    public void CreatePostsView_Fail_When_Unauthenticated() throws Exception {

        mockMvc.perform(get("/posts/create"))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("전체 글 목록 페이지 로딩")
    @Test
    public void AllPostsListView() throws Exception {

        mockMvc.perform(get("/posts/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-posts"));
    }
}