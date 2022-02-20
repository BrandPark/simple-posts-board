package com.brandpark.simplepostsboard.api.posts;

import com.brandpark.simplepostsboard.*;
import com.brandpark.simplepostsboard.modules.OrderBase;
import com.brandpark.simplepostsboard.api.posts.dto.PostsListResponse;
import com.brandpark.simplepostsboard.api.posts.dto.PostsResponse;
import com.brandpark.simplepostsboard.api.posts.dto.PostsSaveRequest;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import com.brandpark.simplepostsboard.modules.blocks.BlockState;
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
    @Autowired BlocksFactory blocksFactory;
    Accounts loginUser;

    @BeforeEach
    public void setUp() {
        loginUser = accountFactory.createAndPersistAccount("user", "1q2w3e4r");
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

    @DisplayName("전체 글 목록 조회하기 - 성공(최신순 조회, 로그인하지 않은 상태)")
    @Test
    public void GetAllOrderByCreatedDateDescPosts_Success_When_Unauthenticated() throws Exception {

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

    @DisplayName("전체 글 목록 조회하기 - 성공(조회수순 조회, 로그인하지 않은 상태)")
    @Test
    public void GetAllOrderByViewCountDescPosts_Success_When_Unauthenticated() throws Exception {

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

    @WithUserDetails(value="user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("전체 글 목록 조회하기 - 성공(차단자 제외하고 최신순 조회, 로그인한 상태)")
    @Test
    public void GetAllOrderByCreatedDateDescPosts_ExcludeBlocked_Success_When_Authenticated() throws Exception {

        // given
        Accounts writer = accountFactory.createAndPersistAccount("writer", "1q2w3e4r");

        List<Posts> notBlockedPosts = postsFactory.createAndPersistPostsList("제목", "내용", writer, 10);

        Accounts blockedByMe = accountFactory.createAndPersistAccount("내가 차단한 사용자", "1q2w3e4r");
        postsFactory.createAndPersistPosts("차단한 사용자의 게시글", "내용", blockedByMe);
        blocksFactory.createAndPersistRelation(loginUser, blockedByMe, BlockState.BLOCKED);

        Accounts blockedMe = accountFactory.createAndPersistAccount("나를 차단한 사용자", "1q2w3e4r");
        postsFactory.createAndPersistPosts("나를 차단한 사용자의 게시글", "내용", blockedMe);
        blocksFactory.createAndPersistRelation(blockedMe, loginUser, BlockState.BLOCKED);

        // when, then
        mockMvc.perform(get("/api/v1/posts")
                        .param("orderBase", OrderBase.DATE_DESC.toString()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

                    PostsListResponse responseObj = objectMapper.readValue(json, PostsListResponse.class);

                    assertThat(responseObj.getItemCount()).isEqualTo(notBlockedPosts.size());

                    List<PostsResponse> findAllPosts = responseObj.getItemList();
                    for (PostsResponse p : findAllPosts) {
                        AssertUtil.assertObjectPropIsNotNull(p);
                    }

                    assertThat(findAllPosts.get(0).getCreatedDate())
                            .isAfterOrEqualTo(findAllPosts.get(notBlockedPosts.size() - 1).getCreatedDate());
                });
    }

    @WithUserDetails(value="user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("전체 글 목록 조회하기 - 성공(차단자 제외하고 조회수순 조회, 로그인한 상태)")
    @Test
    public void GetAllOrderByViewCountDescPosts_ExcludeBlocked_Success_When_Authenticated() throws Exception {

        // given
        Accounts writer = accountFactory.createAndPersistAccount("writer", "1q2w3e4r");

        int notBlockedPostsCount = 10;
        for (int i = 0; i < notBlockedPostsCount; i++) {
            int viewCount = notBlockedPostsCount - i;  // 최신 글일 수록 viewCount를 낮게 준다.
            postsFactory.createAndPersistPosts("제목" + i, "내용" + i, writer, viewCount);
        }

        Accounts blockedByMe = accountFactory.createAndPersistAccount("내가 차단한 사용자", "1q2w3e4r");
        postsFactory.createAndPersistPosts("내가 차단한 사용자의 게시글", "내용", blockedByMe);
        blocksFactory.createAndPersistRelation(loginUser, blockedByMe, BlockState.BLOCKED);

        Accounts blockedMe = accountFactory.createAndPersistAccount("나를 차단한 사용자", "1q2w3e4r");
        postsFactory.createAndPersistPosts("나를 차단한 사용자의 게시글", "내용", blockedMe);
        blocksFactory.createAndPersistRelation(blockedMe, loginUser, BlockState.BLOCKED);

        // when
        mockMvc.perform(get("/api/v1/posts")
                        .param("orderBase", OrderBase.VIEW_DESC.toString()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

                    PostsListResponse responseObj = objectMapper.readValue(json, PostsListResponse.class);

                    assertThat(responseObj.getItemCount()).isEqualTo(notBlockedPostsCount);

                    List<PostsResponse> findAllPosts = responseObj.getItemList();
                    for (PostsResponse p : findAllPosts) {
                        AssertUtil.assertObjectPropIsNotNull(p);
                    }

                    assertThat(findAllPosts.get(0).getViewCount())
                            .isGreaterThan(findAllPosts.get(notBlockedPostsCount - 1).getViewCount());
                });
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