package com.brandpark.simplepostsboard.modules.posts;

import com.brandpark.simplepostsboard.api.OrderBase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomPostsRepositoryImpl implements CustomPostsRepository {

    private final EntityManager em;

    @Override
    public List<Posts> findAllPostsWithAccountsOrderBy(OrderBase orderBase) {

        StringBuilder query = new StringBuilder("SELECT p FROM Posts p JOIN FETCH p.accounts ORDER BY");
        query.append(" p.").append(orderBase.getValue());
        query.append(" ").append(orderBase.getDir());

        return em.createQuery(query.toString(), Posts.class).getResultList();
    }
}
