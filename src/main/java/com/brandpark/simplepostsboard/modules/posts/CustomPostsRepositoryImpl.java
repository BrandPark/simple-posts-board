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

        return em.createQuery(query, Posts.class).getResultList();
    }
}
