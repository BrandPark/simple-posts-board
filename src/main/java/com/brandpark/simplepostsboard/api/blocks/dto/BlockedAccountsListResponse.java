package com.brandpark.simplepostsboard.api.blocks.dto;

import com.brandpark.simplepostsboard.infra.config.SessionAccounts;
import com.brandpark.simplepostsboard.modules.blocks.Blocks;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class BlockedAccountsListResponse {

    private Long fromAccountsId;
    private String fromAccountsNickname;
    private int itemCount;
    private List<BlockedAccountsResponse> itemList;

    public BlockedAccountsListResponse(List<Blocks> blocks, SessionAccounts loginAccounts) {
        fromAccountsId = loginAccounts.getId();
        fromAccountsNickname = loginAccounts.getNickname();
        itemCount = blocks.size();
        itemList = blocks.stream()
                .map(BlockedAccountsResponse::new)
                .collect(Collectors.toList());
    }
}
