package com.brandpark.simplepostsboard;

import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import com.brandpark.simplepostsboard.modules.blocks.BlockState;
import com.brandpark.simplepostsboard.modules.blocks.Blocks;
import com.brandpark.simplepostsboard.modules.blocks.BlocksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class BlocksFactory {

    @Autowired BlocksRepository blocksRepository;

    public Blocks createAndPersistRelation(Accounts from, Accounts to, BlockState state) {
        Blocks relation = Blocks.createBlockRelation(from, to);
        relation.updateState(state);

        return blocksRepository.save(relation);
    }
}
