package com.brandpark.simplepostsboard.api.posts.dto;

import com.brandpark.simplepostsboard.modules.posts.Posts;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostsListResponse {

    private final int itemCount;
    private final List<PostsResponse> itemList;

    public PostsListResponse(List<Posts> allPosts) {
        itemCount = allPosts.size();
        itemList = allPosts.stream()
                .map(PostsResponse::new)
                .collect(Collectors.toList());
    }
}

