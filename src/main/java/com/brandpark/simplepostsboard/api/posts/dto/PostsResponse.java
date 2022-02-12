package com.brandpark.simplepostsboard.api.posts.dto;

import com.brandpark.simplepostsboard.modules.posts.Posts;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostsResponse {
    private Long id;
    private String title;
    private Long writerId;
    private String writerNickname;
    private int viewCount;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public PostsResponse(Posts posts) {

    }
}
