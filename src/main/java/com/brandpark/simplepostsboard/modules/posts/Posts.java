package com.brandpark.simplepostsboard.modules.posts;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Posts {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posts_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "posts_content", nullable = false)
    private String content;

    @Column
    private long viewCount;
}
