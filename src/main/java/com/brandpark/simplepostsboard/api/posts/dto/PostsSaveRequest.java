package com.brandpark.simplepostsboard.api.posts.dto;

import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import com.brandpark.simplepostsboard.modules.posts.Posts;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class PostsSaveRequest {

    @NotEmpty
    @Length(max = 100)
    private String title;

    @NotEmpty
    @Length(max = 255)
    private String content;

    public Posts toEntity(Accounts writer) {
        return Posts.createPosts(title, content, writer);
    }
}
