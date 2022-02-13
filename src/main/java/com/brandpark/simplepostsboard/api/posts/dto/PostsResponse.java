package com.brandpark.simplepostsboard.api.posts.dto;

import com.brandpark.simplepostsboard.modules.posts.Posts;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonPropertyOrder({"id", "title", "writerId", "writerNickname", "viewCount", "createdDate", "modifiedDate"})
@NoArgsConstructor
@Data
public class PostsResponse {
    private Long id;
    private String title;
    private String content;
    private Long writerId;
    private String writerNickname;
    private long viewCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    public PostsResponse(Posts posts) {
        id = posts.getId();
        title = posts.getTitle();
        content = posts.getContent();
        writerId = posts.getAccounts().getId();
        writerNickname = posts.getAccounts().getNickname();
        viewCount = posts.getViewCount();
        createdDate = posts.getCreatedDate();
        modifiedDate = posts.getModifiedDate();
    }
}
