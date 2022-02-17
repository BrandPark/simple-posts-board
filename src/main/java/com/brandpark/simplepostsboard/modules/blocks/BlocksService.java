package com.brandpark.simplepostsboard.modules.blocks;

import com.brandpark.simplepostsboard.infra.config.SessionAccounts;
import com.brandpark.simplepostsboard.modules.accounts.AccountRepository;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BlocksService {

    private final BlocksRepository blocksRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Long createBlockRelation(SessionAccounts loginAccounts, Long toAccountsId) {

        Accounts from = accountRepository.findById(loginAccounts.getId())
                .orElseThrow(() -> new IllegalStateException("사용자의 계정이 올바르지 않습니다."));

        Accounts to = accountRepository.findById(toAccountsId)
                .orElseThrow(() -> new IllegalStateException("차단한 사용자의 계정이 존재하지 않습니다."));

        if (from == to) {
            throw new IllegalArgumentException("자신의 계정은 차단할 수 없습니다.");
        }

        Optional<Blocks> findBlocks = blocksRepository.findByFromAccountsIdAndToAccountsId(from.getId(), to.getId());

        if (findBlocks.isPresent()) {
            findBlocks.get().updateState(BlockState.BLOCKED);

            return findBlocks.get().getId();
        }

        return blocksRepository.save(Blocks.createBlockRelation(from, to)).getId();
    }

    @Transactional
    public Long createUnblockRelation(SessionAccounts loginAccounts, Long toAccountsId) {

        Accounts from = accountRepository.findById(loginAccounts.getId())
                .orElseThrow(() -> new IllegalStateException("사용자의 계정이 올바르지 않습니다."));

        Accounts to = accountRepository.findById(toAccountsId)
                .orElseThrow(() -> new IllegalStateException("차단한 사용자의 계정이 존재하지 않습니다."));

        Optional<Blocks> findBlocks = blocksRepository.findByFromAccountsIdAndToAccountsId(from.getId(), to.getId());

        if (findBlocks.isEmpty()) {
            throw new IllegalStateException("차단되지 않은 계정입니다.");
        } else if(findBlocks.get().getBlockState() == BlockState.NOT_BLOCKED) {
            throw new IllegalStateException("이미 차단해제 되어 있습니다.");
        }

        Blocks blockRelation = findBlocks.get();

        blockRelation.updateState(BlockState.NOT_BLOCKED);

        return blockRelation.getId();
    }
}
