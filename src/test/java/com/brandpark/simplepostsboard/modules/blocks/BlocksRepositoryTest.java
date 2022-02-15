package com.brandpark.simplepostsboard.modules.blocks;

import com.brandpark.simplepostsboard.AccountFactory;
import com.brandpark.simplepostsboard.BlocksFactory;
import com.brandpark.simplepostsboard.RepoTest;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RepoTest
class BlocksRepositoryTest {

    @Autowired AccountFactory accountFactory;
    @Autowired BlocksRepository blocksRepository;
    @Autowired BlocksFactory blocksFactory;
    @Autowired EntityManager entityManager;

    @DisplayName("fromAccountId와 toAccountId로 Blocks Entity 가져오기")
    @Test
    public void FindBlocksByFromAccountsIdAndToAccountsId() throws Exception {

        // given
        Accounts from = accountFactory.createAndPersistAccount("차단자", "1q2w3e4r");
        Accounts to = accountFactory.createAndPersistAccount("차단당한사람", "1q2w3e4r");

        blocksRepository.save(Blocks.createBlockRelation(from, to));

        entityManager.flush();
        entityManager.clear();

        // when
        Optional<Blocks> result = blocksRepository.findByFromAccountsIdAndToAccountsId(from.getId(), to.getId());

        // then
        assertThat(result).isNotEmpty();
    }

    @DisplayName("Block 상태의 목록 모두 가져오기")
    @Test
    public void FindAllByFromAccountsIdAndBlockStateBlocked() throws Exception {

        // given
        Accounts from = accountFactory.createAndPersistAccount("차단자", "1q2w3e4r");

        int blockedCount = 5;
        List<Accounts> blockedAccountsList = accountFactory.createAndPersistAccountList("차단 당하는 사람", "1q2w3e4r", blockedCount);

        int notBlockedCount = 7;
        accountFactory.createAndPersistAccountList("차단 당하는 사람", "1q2w3e4r", notBlockedCount);

        for (Accounts blockedAccounts : blockedAccountsList) {
            blocksFactory.createAndPersistRelation(from, blockedAccounts, BlockState.BLOCKED);
        }

        // when
        List<Blocks> result = blocksRepository.findAllByFromAccountsIdAndBlockStateBlocked(from.getId());

        // then
        assertThat(result.size()).isEqualTo(blockedCount);
    }
}