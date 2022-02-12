package com.brandpark.simplepostsboard.api;

import com.brandpark.simplepostsboard.modules.BaseTimeEntity;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(
        uniqueConstraints = @UniqueConstraint(name = "UK_BLOCK_LIST", columnNames = {"accounts_id", "target_accounts_id"})
)
@Entity
public class BlockList extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_list_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "accounts_id")
    private Accounts accounts;

    @ManyToOne
    @JoinColumn(name = "target_accounts_id")
    private Accounts target;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BlockState blockState;
}
