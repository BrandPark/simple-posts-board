package com.brandpark.simplepostsboard.api;

import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Comments {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comments_id")
    private Long id;

    @JoinColumn(name = "accounts_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Accounts accounts;

    @Column(name = "comments_content", length = 100, nullable = false)
    private String content;
}
