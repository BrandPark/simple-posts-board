package com.brandpark.simplepostsboard.modules.commnets;

import com.brandpark.simplepostsboard.modules.BaseTimeEntity;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import com.brandpark.simplepostsboard.modules.posts.Posts;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Comments extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comments_id")
    private Long id;

    @Column(name = "comments_content", length = 100, nullable = false)
    private String content;

    @JoinColumn(name = "accounts_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Accounts accounts;

    @JoinColumn(name = "posts_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Posts posts;

    public static Comments createComments(String content, Accounts accounts, Posts posts) {
        Comments c = new Comments();

        c.content = content;
        c.accounts = accounts;
        c.posts = posts;

        return c;
    }
}
