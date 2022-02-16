package com.brandpark.simplepostsboard.modules.commnets;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CommentsRepository extends JpaRepository<Comments, Long>, CustomCommentsRepository {

    List<Comments> findAllByPostsId(Long postsId);

    @Override
    List<Comments> findAllNotBlockedCommentsByPostsId(Long loginAccountsId, Long postsId);
}
