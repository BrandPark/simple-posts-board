package com.brandpark.simplepostsboard.api.blocks;

import com.brandpark.simplepostsboard.AccountFactory;
import com.brandpark.simplepostsboard.AssertUtil;
import com.brandpark.simplepostsboard.BlocksFactory;
import com.brandpark.simplepostsboard.MockMvcTest;
import com.brandpark.simplepostsboard.api.blocks.dto.BlockedAccountsListResponse;
import com.brandpark.simplepostsboard.api.blocks.dto.BlockedAccountsResponse;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import com.brandpark.simplepostsboard.modules.blocks.BlockState;
import com.brandpark.simplepostsboard.modules.blocks.Blocks;
import com.brandpark.simplepostsboard.modules.blocks.BlocksRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
class BlocksApiControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired AccountFactory accountFactory;
    @Autowired BlocksRepository blocksRepository;
    @Autowired BlocksFactory blocksFactory;
    Accounts loginUser;

    @BeforeEach
    public void setUp() {
        loginUser = accountFactory.createAndPersistAccount("loginUser", "1q2w3e4r");
    }

    // TODO: 실패 처리. 에러 핸들러 작성 후 테스트 코드 작성

    @WithUserDetails(value = "loginUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("차단하기 - 성공(처음 차단)")
    @Test
    public void BlocksAccounts_Success_FirstBlock() throws Exception {

        // given
        Accounts from = loginUser;
        Accounts to = accountFactory.createAndPersistAccount("차단당하는 사람", "1q2w3e4r");

        // when, then
        mockMvc.perform(post("/api/v1/accounts/" + to.getId() + "/block"))
                .andExpect(status().isOk())
                .andExpect(result -> {

                    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

                    Long relationId = objectMapper.readValue(json, Long.class);

                    assertThat(relationId).isNotNull();
                });

        Blocks relation = blocksRepository.findAll().get(0);

        AssertUtil.assertObjectPropIsNotNull(relation);
        assertThat(relation.getFromAccounts().getId()).isEqualTo(from.getId());
        assertThat(relation.getToAccounts().getId()).isEqualTo(to.getId());
        assertThat(relation.getBlockState()).isEqualTo(BlockState.BLOCKED);
    }

    @WithUserDetails(value = "loginUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("차단하기 - 성공(차단 해제했던 대상을 다시 차단)")
    @Test
    public void BlocksAccounts_Success_ReBlock() throws Exception {

        // given
        Accounts from = loginUser;
        Accounts to = accountFactory.createAndPersistAccount("차단당하는 사람", "1q2w3e4r");

        blocksFactory.createAndPersistRelation(from, to, BlockState.NOT_BLOCKED);

        // when, then
        mockMvc.perform(post("/api/v1/accounts/" + to.getId() + "/block"))
                .andExpect(status().isOk())
                .andExpect(result -> {

                    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

                    Long relationId = objectMapper.readValue(json, Long.class);

                    assertThat(relationId).isNotNull();
                });

        Blocks relation = blocksRepository.findAll().get(0);

        AssertUtil.assertObjectPropIsNotNull(relation);
        assertThat(relation.getFromAccounts().getId()).isEqualTo(from.getId());
        assertThat(relation.getToAccounts().getId()).isEqualTo(to.getId());
        assertThat(relation.getBlockState()).isEqualTo(BlockState.BLOCKED);
    }

    @WithUserDetails(value = "loginUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("차단 해제하기 - 성공")
    @Test
    public void UnblocksAccounts_Success() throws Exception {

        // given
        Accounts from = loginUser;
        Accounts to = accountFactory.createAndPersistAccount("차단당하는 사람", "1q2w3e4r");

        Blocks blockedRelation = blocksRepository.save(Blocks.createBlockRelation(from, to));

        assertThat(blockedRelation.getBlockState()).isEqualTo(BlockState.BLOCKED);

        // when, then
        mockMvc.perform(post("/api/v1/accounts/" + to.getId() + "/unblock"))
                .andExpect(status().isOk())
                .andExpect(result -> {

                    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

                    Long relationId = objectMapper.readValue(json, Long.class);

                    assertThat(relationId).isNotNull();
                });

        Blocks relation = blocksRepository.findAll().get(0);

        AssertUtil.assertObjectPropIsNotNull(relation);
        assertThat(relation.getFromAccounts().getId()).isEqualTo(from.getId());
        assertThat(relation.getToAccounts().getId()).isEqualTo(to.getId());
        assertThat(relation.getBlockState()).isEqualTo(BlockState.NOT_BLOCKED);
    }

    // TODO: 로그인하지 않은 경우등의 에러처리 코드 삽입

    @WithUserDetails(value = "loginUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("차단 리스트 모두 조회하기 - 성공")
    @Test
    public void FindAllBlocksList_Fail_Unauthenticated() throws Exception {

        // given
        Accounts from = loginUser;

        int blockedCount = 5;
        List<Accounts> blockedAccountsList = accountFactory.createAndPersistAccountList("차단 당하는 사람", "1q2w3e4r", blockedCount);

        int notBlockedCount = 7;
        accountFactory.createAndPersistAccountList("차단 당하는 사람", "1q2w3e4r", notBlockedCount);

        for (Accounts blockedAccounts : blockedAccountsList) {
            blocksFactory.createAndPersistRelation(from, blockedAccounts, BlockState.BLOCKED);
        }

        // when, then
        mockMvc.perform(get("/api/v1/accounts/" + loginUser.getId() + "blocks"))
                .andExpect(status().isOk())
                .andExpect(result -> {

                    String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

                    BlockedAccountsListResponse response = objectMapper.readValue(json, BlockedAccountsListResponse.class);

                    assertThat(response.getItemCount()).isEqualTo(blockedCount);
                    assertThat(response.getFromAccountsId()).isEqualTo(loginUser.getId());
                    assertThat(response.getFromAccountsNickname()).isEqualTo(loginUser.getNickname());

                    List<BlockedAccountsResponse> blockedList = response.getItemList();
                    assertThat(blockedList.size()).isEqualTo(blockedCount);
                    for (BlockedAccountsResponse b : blockedList) {
                        AssertUtil.assertObjectPropIsNotNull(b);
                    }
                });
    }
}