package com.brandpark.simplepostsboard.api.blocks;

import com.brandpark.simplepostsboard.api.blocks.dto.BlockedAccountsListResponse;
import com.brandpark.simplepostsboard.infra.config.SessionAccounts;
import com.brandpark.simplepostsboard.modules.accounts.LoginAccounts;
import com.brandpark.simplepostsboard.modules.blocks.BlockState;
import com.brandpark.simplepostsboard.modules.blocks.Blocks;
import com.brandpark.simplepostsboard.modules.blocks.BlocksRepository;
import com.brandpark.simplepostsboard.modules.blocks.BlocksService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class BlocksApiController {

    private final BlocksService blocksService;
    private final BlocksRepository blocksRepository;

    @PostMapping("/accounts/{toAccountsId}/block")
    public Long createBlockRelation(@LoginAccounts SessionAccounts loginAccounts, @PathVariable Long toAccountsId) {

        return blocksService.createBlockRelation(loginAccounts, toAccountsId);
    }

    @PostMapping("/accounts/{toAccountsId}/unblock")
    public Long createUnblockRelation(@LoginAccounts SessionAccounts loginAccounts, @PathVariable Long toAccountsId) {

        return blocksService.createUnblockRelation(loginAccounts, toAccountsId);
    }

    @GetMapping("/accounts/{accountsId}/blocks")
    public BlockedAccountsListResponse getAllBlockedAccountsList(@PathVariable Long accountsId, @LoginAccounts SessionAccounts loginAccounts) {

        if (!accountsId.equals(loginAccounts.getId())) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        List<Blocks> allBlockRelation = blocksRepository.findAllByFromAccountsIdAndBlockState(loginAccounts.getId(), BlockState.BLOCKED);

        return new BlockedAccountsListResponse(allBlockRelation, loginAccounts);
    }
}
