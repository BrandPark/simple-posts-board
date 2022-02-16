package com.brandpark.simplepostsboard.modules.commnets;

import java.util.List;

public interface CustomCommentsRepository {

    List<Comments> findAllNotBlockedCommentsByPostsId(Long loginAccountsId, Long postsId);
}
