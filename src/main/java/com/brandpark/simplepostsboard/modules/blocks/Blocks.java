package com.brandpark.simplepostsboard.modules.blocks;

import com.brandpark.simplepostsboard.modules.BaseTimeEntity;
import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(
        uniqueConstraints = @UniqueConstraint(name = "UK_BLOCK_LIST", columnNames = {"from_accounts_id", "to_accounts_id"})
)
@Entity
public class Blocks extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blocks_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_accounts_id", nullable = false)
    private Accounts fromAccounts;

    @ManyToOne
    @JoinColumn(name = "to_accounts_id", nullable = false)
    private Accounts toAccounts;

    @Enumerated(EnumType.STRING)
    @Column(name = "block_state", nullable = false)
    private BlockState blockState;

    public static Blocks createBlockRelation(Accounts from, Accounts to) {
        Blocks b = new Blocks();
        b.fromAccounts = from;
        b.toAccounts = to;
        b.blockState = BlockState.BLOCKED;

        return b;
    }

    public void updateState(BlockState state) {
        if(blockState != state)
            blockState = state;
    }
}
