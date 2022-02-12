package com.brandpark.simplepostsboard;


import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import com.brandpark.simplepostsboard.modules.posts.Posts;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AssertUtil {

    public static void assertObject(Posts posts) {
        assertThat(posts.getId()).isNotNull();
        assertThat(posts.getTitle()).isNotNull();
        assertThat(posts.getContent()).isNotNull();
        assertThat(posts.getCreatedDate()).isNotNull();
        assertThat(posts.getModifiedDate()).isNotNull();
        assertThat(posts.getViewCount()).isGreaterThanOrEqualTo(0);
    }

    public static void assertObject(Accounts accounts) {
        assertThat(accounts.getId()).isNotNull();
        assertThat(accounts.getNickname()).isNotNull();
        assertThat(accounts.getPassword()).isNotNull();
        assertThat(accounts.getCreatedDate()).isNotNull();
        assertThat(accounts.getModifiedDate()).isNotNull();
    }
}
