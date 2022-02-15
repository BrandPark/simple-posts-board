package com.brandpark.simplepostsboard.api.blocks.dto;

import com.brandpark.simplepostsboard.modules.blocks.Blocks;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class BlockedAccountsListResponse {
    private int itemCount;
    private List<BlockedAccountsResponse> itemList;

    public BlockedAccountsListResponse(List<Blocks> blocks) {
        itemCount = blocks.size();
        itemList = blocks.stream()
                .map(BlockedAccountsResponse::new)
                .collect(Collectors.toList());
    }
}
