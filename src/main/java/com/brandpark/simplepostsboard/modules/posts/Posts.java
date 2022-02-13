package com.brandpark.simplepostsboard.modules.posts;

import com.brandpark.simplepostsboard.modules.BaseTimeEntity;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter
@Entity
public class Posts extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posts_id")
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "posts_content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accounts_id", nullable = false)
    private Accounts accounts;

    @Column(name = "view_count")
    private long viewCount;

    public static Posts createPosts(String title, String content, Accounts accounts) {
        Posts posts = new Posts();
        posts.title = title;
        posts.content = content;
        posts.accounts = accounts;

        return posts;
    }
}
