package com.brandpark.simplepostsboard.modules.commnets;

import com.brandpark.simplepostsboard.modules.posts.Posts;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomCommentsRepositoryImpl implements CustomCommentsRepository {

    private final EntityManager em;

    @Override
    public List<Comments> findAllNotBlockedCommentsByPostsId(Long loginAccountsId, Long postsId) {

        return em.createQuery(
                "SELECT c FROM Comments c " +
                        "JOIN FETCH c.accounts " +
                        "WHERE NOT EXISTS(" +
                            "SELECT b FROM Blocks b " +
                            "WHERE b.blockState = 'BLOCKED' " +
                            "AND (" +
                                "(c.accounts.id = b.toAccounts.id AND b.fromAccounts.id = :loginAccountsId) " +
                                "OR (c.accounts.id = b.fromAccounts.id AND b.toAccounts.id = :loginAccountsId)" +
                            ")" +
                        ") " +
                        "AND c.posts.id = :postsId", Comments.class)
                .setParameter("loginAccountsId", loginAccountsId)
                .setParameter("postsId", postsId)
                .getResultList();
    }
}
