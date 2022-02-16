package com.brandpark.simplepostsboard;


import com.brandpark.simplepostsboard.api.blocks.dto.BlockedAccountsResponse;
import com.brandpark.simplepostsboard.api.comments.dto.CommentsResponse;
import com.brandpark.simplepostsboard.api.posts.dto.PostsResponse;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import com.brandpark.simplepostsboard.modules.blocks.Blocks;
import com.brandpark.simplepostsboard.modules.commnets.Comments;
import com.brandpark.simplepostsboard.modules.posts.Posts;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AssertUtil {

    public static void assertObjectPropIsNotNull(Posts posts) {
        assertThat(posts.getId()).isNotNull();
        assertThat(posts.getTitle()).isNotNull();
        assertThat(posts.getContent()).isNotNull();
        assertThat(posts.getCreatedDate()).isNotNull();
        assertThat(posts.getModifiedDate()).isNotNull();
        assertThat(posts.getViewCount()).isGreaterThanOrEqualTo(0);
    }

    public static void assertObjectPropIsNotNull(Accounts accounts) {
        assertThat(accounts.getId()).isNotNull();
        assertThat(accounts.getNickname()).isNotNull();
        assertThat(accounts.getPassword()).isNotNull();
        assertThat(accounts.getCreatedDate()).isNotNull();
        assertThat(accounts.getModifiedDate()).isNotNull();
    }

    public static void assertObjectPropIsNotNull(PostsResponse obj) {
        assertThat(obj.getPostsId()).isNotNull();
        assertThat(obj.getTitle()).isNotNull();
        assertThat(obj.getWriterId()).isNotNull();
        assertThat(obj.getWriterNickname()).isNotNull();
        assertThat(obj.getViewCount()).isNotNull();
        assertThat(obj.getCreatedDate()).isNotNull();
        assertThat(obj.getModifiedDate()).isNotNull();
    }

    public static void assertObjectPropIsNotNull(Comments obj) {
        assertThat(obj.getId()).isNotNull();
        assertThat(obj.getContent()).isNotNull();
        assertThat(obj.getPosts()).isNotNull();
        assertThat(obj.getAccounts()).isNotNull();
        assertThat(obj.getCreatedDate()).isNotNull();
        assertThat(obj.getModifiedDate()).isNotNull();
    }

    public static void assertObjectPropIsNotNull(CommentsResponse obj) {
        assertThat(obj.getCommentsId()).isNotNull();
        assertThat(obj.getContent()).isNotNull();
        assertThat(obj.getWriterId()).isNotNull();
        assertThat(obj.getWriterNickname()).isNotNull();
        assertThat(obj.getCreatedDate()).isNotNull();
        assertThat(obj.getModifiedDate()).isNotNull();
    }

    public static void assertObjectPropIsNotNull(Blocks obj) {
        assertThat(obj.getId()).isNotNull();
        assertThat(obj.getFromAccounts()).isNotNull();
        assertThat(obj.getToAccounts()).isNotNull();
        assertThat(obj.getBlockState()).isNotNull();
        assertThat(obj.getCreatedDate()).isNotNull();
        assertThat(obj.getModifiedDate()).isNotNull();
    }

    public static void assertObjectPropIsNotNull(BlockedAccountsResponse obj) {
        assertThat(obj.getBlocksId()).isNotNull();
        assertThat(obj.getBlockedAccountsId()).isNotNull();
        assertThat(obj.getBlockedAccountsNickname()).isNotNull();
        assertThat(obj.getCreatedDate()).isNotNull();
        assertThat(obj.getModifiedDate()).isNotNull();
    }
}
