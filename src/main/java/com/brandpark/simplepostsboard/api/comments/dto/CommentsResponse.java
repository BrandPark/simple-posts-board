package com.brandpark.simplepostsboard.api.comments.dto;

import com.brandpark.simplepostsboard.modules.commnets.Comments;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class CommentsResponse {
    private Long commentsId;
    private String content;
    private Long writerId;
    private String writerNickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    public CommentsResponse(Comments comments) {
        commentsId = comments.getId();
        content = comments.getContent();
        writerId = comments.getAccounts().getId();
        writerNickname = comments.getAccounts().getNickname();
        createdDate = comments.getCreatedDate();
        modifiedDate = comments.getModifiedDate();
    }
}
