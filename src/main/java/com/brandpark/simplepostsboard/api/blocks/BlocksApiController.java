package com.brandpark.simplepostsboard.api.blocks;

import com.brandpark.simplepostsboard.api.blocks.dto.BlockedAccountsListResponse;
import com.brandpark.simplepostsboard.infra.config.SessionAccounts;
import com.brandpark.simplepostsboard.modules.accounts.LoginAccounts;
import com.brandpark.simplepostsboard.modules.blocks.BlocksRepository;
import com.brandpark.simplepostsboard.modules.blocks.BlocksService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/blocks")
    public BlockedAccountsListResponse getAllBlockedAccountsList(@LoginAccounts SessionAccounts loginAccounts) {
        return null;
    }
}
