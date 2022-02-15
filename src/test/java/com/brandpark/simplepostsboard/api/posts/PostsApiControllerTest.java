package com.brandpark.simplepostsboard.api.posts;

import com.brandpark.simplepostsboard.AccountFactory;
import com.brandpark.simplepostsboard.AssertUtil;
import com.brandpark.simplepostsboard.MockMvcTest;
import com.brandpark.simplepostsboard.PostsFactory;
import com.brandpark.simplepostsboard.modules.OrderBase;
import com.brandpark.simplepostsboard.api.posts.dto.PostsListResponse;
import com.brandpark.simplepostsboard.api.posts.dto.PostsResponse;
import com.brandpark.simplepostsboard.api.posts.dto.PostsSaveRequest;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import com.brandpark.simplepostsboard.modules.posts.Posts;
import com.brandpark.simplepostsboard.modules.posts.PostsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class PostsApiControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired AccountFactory accountFactory;
    @Autowired PostsRepository postsRepository;
    @Autowired PostsFactory postsFactory;

    @BeforeEach
    public void setUp() {
        accountFactory.createAndPersistAccount("user", "1q2w3e4r");
    }

    @DisplayName("글 등록하기 - 실패(인증되지 않은 요청)")
    @Test
    public void RegisterPosts_Fail_When_Unauthenticated() throws Exception {

        // given
        PostsSaveRequest req = new PostsSaveRequest();
        req.setTitle("제목");
        req.setContent("내용");

        // when, then
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is3xxRedirection());
        // TODO: JWT 도입 후 필터나 인터셉터에서 리디렉션이 아니라 예외 처리해야 합니다.
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("글 등록하기 - 성공(인증된 요청)")
    @Test
    public void RegisterPosts_Fail_When_Authenticated() throws Exception {

        // given
        PostsSaveRequest req = new PostsSaveRequest();
        req.setTitle("제목");
        req.setContent("내용");

        // when, then
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    Long postsId = objectMapper.readValue(json, Long.class);

                    assertThat(postsRepository.findById(postsId)).isNotEmpty();
                });
        // TODO: JWT 도입 후 withUserDetails가 아니라 토큰을 넣는 상황을 테스트 해야합니다.
    }

    @DisplayName("전체 글 목록 조회하기 - 성공(최신순 조회)")
    @Test
    public void GetAllPosts_Success_When_OrderBy_CreatedDateDesc() throws Exception {

        // given
        Accounts writer = accountFactory.createAndPersistAccount("writer", "1q2w3e4r");

        List<Posts> allPosts = postsFactory.createAndPersistPostsList("제목", "내용", writer, 10);

        // when
        mockMvc.perform(get("/api/v1/posts")
                        .param("orderBase", OrderBase.DATE_DESC.toString()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

                    PostsListResponse responseObj = objectMapper.readValue(json, PostsListResponse.class);

                    assertThat(responseObj.getItemCount()).isEqualTo(allPosts.size());

                    List<PostsResponse> findAllPosts = responseObj.getItemList();
                    for (PostsResponse p : findAllPosts) {
                        AssertUtil.assertObjectPropIsNotNull(p);
                    }

                    assertThat(findAllPosts.get(0).getCreatedDate())
                            .isAfterOrEqualTo(findAllPosts.get(allPosts.size() - 1).getCreatedDate());
                });

        // then
    }

    @DisplayName("전체 글 목록 조회하기 - 성공(조회수순 조회)")
    @Test
    public void GetAllPosts_Success_When_OrderBy_ViewCountDesc() throws Exception {

        // given
        Accounts writer = accountFactory.createAndPersistAccount("writer", "1q2w3e4r");

        int allPostsCount = 10;
        for (int i = 0; i < allPostsCount; i++) {
            int viewCount = allPostsCount - i;  // 최신 글일 수록 viewCount를 낮게 준다.
            postsFactory.createAndPersistPosts("제목" + i, "내용" + i, writer, viewCount);
        }

        // when
        mockMvc.perform(get("/api/v1/posts")
                        .param("orderBase", OrderBase.VIEW_DESC.toString()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

                    PostsListResponse responseObj = objectMapper.readValue(json, PostsListResponse.class);

                    assertThat(responseObj.getItemCount()).isEqualTo(allPostsCount);

                    List<PostsResponse> findAllPosts = responseObj.getItemList();
                    for (PostsResponse p : findAllPosts) {
                        AssertUtil.assertObjectPropIsNotNull(p);
                    }

                    assertThat(findAllPosts.get(0).getViewCount())
                            .isGreaterThan(findAllPosts.get(allPostsCount - 1).getViewCount());
                });

        // then
    }

    @DisplayName("단일 글 세부 내용 조회 및 조회수 증가 - 성공")
    @Test
    public void FindPostsAndIncreaseViewCount_Success() throws Exception {

        // given
        Accounts writer = accountFactory.createAndPersistAccount("작성자", "1q2w3e4r");
        Posts savedPosts = postsFactory.createAndPersistPosts("제목", "내용", writer);

        assertThat(savedPosts.getViewCount()).isEqualTo(0);

        // when
        mockMvc.perform(get("/api/v1/posts/" + savedPosts.getId()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

                    PostsResponse responseObj = objectMapper.readValue(json, PostsResponse.class);

                    assertThat(savedPosts.getId()).isEqualTo(responseObj.getPostsId());
                    AssertUtil.assertObjectPropIsNotNull(responseObj);
                    assertThat(responseObj.getViewCount()).isEqualTo(1);
                });

        // then
        Posts posts = postsRepository.findById(savedPosts.getId()).get();
        assertThat(posts.getViewCount()).isEqualTo(1);
    }

    // TODO: 단일 글 조회 실패하는 경우 테스트 코드 넣어야함. 예외 핸들러 만들어서 처리
}