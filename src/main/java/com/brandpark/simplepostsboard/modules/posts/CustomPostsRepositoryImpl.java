package com.brandpark.simplepostsboard.modules.posts;

import com.brandpark.simplepostsboard.modules.OrderBase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomPostsRepositoryImpl implements CustomPostsRepository {

    private final EntityManager em;

    @Override
    public List<Posts> findAllOrderedPostsWithAccounts(OrderBase orderBase) {

        String query = String.format(
                "SELECT p FROM Posts p " +
                        "JOIN FETCH p.accounts " +
                        "ORDER BY p.%s %s", orderBase.getValue(), orderBase.getDir());

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<Posts> findAllOrderedPostsWithAccountsExcludeBlockedAccountsPosts(Long loginAccountsId, OrderBase orderBase) {

        String orderQuery = String.format("ORDER BY p.%s %s", orderBase.getValue(), orderBase.getDir());

        return em.createQuery("" +
                        "SELECT p FROM Posts p " +
                        "JOIN FETCH p.accounts " +
                        "WHERE NOT EXISTS(" +
                            "SELECT b FROM Blocks b " +
                            "WHERE b.fromAccounts.id = :loginAccountsId " +
                            "AND b.blockState = 'BLOCKED' " +
                            "AND p.accounts.id = b.toAccounts.id" +
                        ")" +
                        orderQuery, Posts.class)
                .setParameter("loginAccountsId", loginAccountsId)
                .getResultList();
    }
}
