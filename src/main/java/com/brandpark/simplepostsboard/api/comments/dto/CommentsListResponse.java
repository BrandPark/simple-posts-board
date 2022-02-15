package com.brandpark.simplepostsboard.api.comments.dto;

import com.brandpark.simplepostsboard.modules.commnets.Comments;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class CommentsListResponse {
    private int itemCount;
    private List<CommentsResponse> itemList;

    public CommentsListResponse(List<Comments> comments) {
        itemCount = comments.size();
        itemList = comments.stream()
                .map(CommentsResponse::new)
                .collect(Collectors.toList());
    }
}
