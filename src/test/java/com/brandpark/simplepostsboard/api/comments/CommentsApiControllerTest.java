package com.brandpark.simplepostsboard.api.comments;

import com.brandpark.simplepostsboard.*;
import com.brandpark.simplepostsboard.api.comments.dto.CommentsListResponse;
import com.brandpark.simplepostsboard.api.comments.dto.CommentsResponse;
import com.brandpark.simplepostsboard.api.comments.dto.CommentsSaveRequest;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import com.brandpark.simplepostsboard.modules.blocks.BlockState;
import com.brandpark.simplepostsboard.modules.commnets.Comments;
import com.brandpark.simplepostsboard.modules.commnets.CommentsRepository;
import com.brandpark.simplepostsboard.modules.posts.Posts;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class CommentsApiControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountFactory accountFactory;
    @Autowired PostsFactory postsFactory;
    @Autowired CommentsFactory commentsFactory;
    @Autowired ObjectMapper objectMapper;
    @Autowired CommentsRepository commentsRepository;
    @Autowired BlocksFactory blocksFactory;
    Accounts loginAccounts;

    @BeforeEach
    public void setUp() {
        loginAccounts = accountFactory.createAndPersistAccount("user", "1q2w3e4r");
    }

    // TODO: 모든 댓글 조회 실패의 경우 에러 핸들러를 작성하여 처리 후 테스트 코드 작성

    @DisplayName("게시글에 등록된 모든 댓글들 조회하기 - 성공(로그인하지 않은 경우)")
    @Test
    public void FindAllComments_Success_When_Unauthenticated() throws Exception {
    
        // given
        Accounts postsWriter = accountFactory.createAndPersistAccount("글 작성자", "1q2w3e4r");
        Posts posts = postsFactory.createAndPersistPosts("게시글", "내용", postsWriter);

        Accounts commentsWriter = accountFactory.createAndPersistAccount("댓글 작성자", "1q2w3e4r");
        int allCommentsCount = 10;
        commentsFactory.createAndPersistCommentsList("댓글", commentsWriter, posts, allCommentsCount);

        // when, then
        mockMvc.perform(get("/api/v1/posts/" + posts.getId() + "/comments"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

                    CommentsListResponse response = objectMapper.readValue(json, CommentsListResponse.class);
                    assertThat(response.getItemCount()).isEqualTo(allCommentsCount);

                    for (CommentsResponse co : response.getItemList()) {
                        AssertUtil.assertObjectPropIsNotNull(co);
                    }
                });
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("차단된 댓글을 제외하고 게시글에 등록된 모든 댓글들 조회하기 - 성공(로그인한 경우)")
    @Test
    public void FindAllComments_ExcludeBlocked_Success_When_Authenticated() throws Exception {

        // given
        Accounts postsWriter = accountFactory.createAndPersistAccount("글 작성자", "1q2w3e4r");
        Posts posts = postsFactory.createAndPersistPosts("게시글", "내용", postsWriter);

        Accounts commentsWriter = accountFactory.createAndPersistAccount("댓글 작성자", "1q2w3e4r");
        int notBlockedCommentsCount = 10;
        commentsFactory.createAndPersistCommentsList("댓글", commentsWriter, posts, notBlockedCommentsCount);

        Accounts blockedByMe = accountFactory.createAndPersistAccount("내가 차단한 사람", "1q2w3e4r");
        commentsFactory.createAndPersistComments("내가 차단한 사람의 댓글", blockedByMe, posts);
        blocksFactory.createAndPersistRelation(loginAccounts, blockedByMe, BlockState.BLOCKED);

        Accounts blockedMe = accountFactory.createAndPersistAccount("나를 차단한 사람", "1q2w3e4r");
        commentsFactory.createAndPersistComments("나를 차단한 사람의 댓글", blockedMe, posts);
        blocksFactory.createAndPersistRelation(blockedMe, loginAccounts, BlockState.BLOCKED);

        // when, then
        mockMvc.perform(get("/api/v1/posts/" + posts.getId() + "/comments"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

                    CommentsListResponse response = objectMapper.readValue(json, CommentsListResponse.class);
                    assertThat(response.getItemCount()).isEqualTo(notBlockedCommentsCount);

                    for (CommentsResponse co : response.getItemList()) {
                        AssertUtil.assertObjectPropIsNotNull(co);
                    }
                });
    }

    // TODO : 마찬가지로 실패 경우 테스트
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("게시물에 댓글 등록하기 - 성공")
    @Test
    public void RegisterComments_Success() throws Exception {

        // given
        Accounts postsWriter = accountFactory.createAndPersistAccount("글 작성자", "1q2w3e4r");
        Posts posts = postsFactory.createAndPersistPosts("게시글", "내용", postsWriter);

        CommentsSaveRequest reqData = new CommentsSaveRequest();
        reqData.setContent("댓글");

        // when, then
        mockMvc.perform(post("/api/v1/posts/" + posts.getId() + "/comments")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqData)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

                    Long commentsId = objectMapper.readValue(json, Long.class);
                    assertThat(commentsId).isNotNull();

                    Comments newComments = commentsRepository.findById(commentsId).get();
                    AssertUtil.assertObjectPropIsNotNull(newComments);
                });
    }

}