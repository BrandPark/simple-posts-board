package com.brandpark.simplepostsboard.api.blocks.dto;

import com.brandpark.simplepostsboard.modules.blocks.Blocks;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class BlockedAccountsResponse {
    private Long blocksId;
    private Long blockedAccountsId;
    private String blockedAccountsNickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    public BlockedAccountsResponse(Blocks blocks) {
        blocksId = blocks.getId();
        blockedAccountsId = blocks.getToAccounts().getId();
        blockedAccountsNickname = blocks.getToAccounts().getNickname();
        createdDate = blocks.getCreatedDate();
        modifiedDate = blocks.getModifiedDate();
    }
}
