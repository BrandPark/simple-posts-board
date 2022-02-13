package com.brandpark.simplepostsboard.api.posts.dto;

import com.brandpark.simplepostsboard.modules.posts.Posts;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@JsonPropertyOrder({"itemCount", "itemList"})
@NoArgsConstructor
@Data
public class PostsListResponse {

    private int itemCount;
    private List<PostsResponse> itemList;

    public PostsListResponse(List<Posts> allPosts) {
        itemCount = allPosts.size();
        itemList = allPosts.stream()
                .map(PostsResponse::new)
                .collect(Collectors.toList());
    }
}

