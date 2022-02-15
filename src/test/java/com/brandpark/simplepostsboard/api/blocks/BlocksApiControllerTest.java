package com.brandpark.simplepostsboard.api.blocks;

import com.brandpark.simplepostsboard.AccountFactory;
import com.brandpark.simplepostsboard.AssertUtil;
import com.brandpark.simplepostsboard.MockMvcTest;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class BlocksApiControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired AccountFactory accountFactory;
    @Autowired BlocksRepository blocksRepository;
    Accounts loginUser;

    @BeforeEach
    public void setUp() {
        loginUser = accountFactory.createAndPersistAccount("loginUser", "1q2w3e4r");
    }

    // TODO: 실패 처리. 에러 핸들러 작성 후 테스트 코드 작성

    @WithUserDetails(value = "loginUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("차단하기 - 성공")
    @Test
    public void BlocksAccounts_Success() throws Exception {

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
}