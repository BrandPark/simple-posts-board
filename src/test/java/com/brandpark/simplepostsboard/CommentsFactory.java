package com.brandpark.simplepostsboard;

import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import com.brandpark.simplepostsboard.modules.commnets.Comments;
import com.brandpark.simplepostsboard.modules.commnets.CommentsRepository;
import com.brandpark.simplepostsboard.modules.posts.Posts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Profile("test")
@Component
public class CommentsFactory {

    @Autowired private CommentsRepository commentsRepository;

    public Comments createAndPersistComments(String content, Accounts accounts, Posts posts) {
        return commentsRepository.save(Comments.createComments(content, accounts, posts));
    }

    public List<Comments> createAndPersistCommentsList(String content, Accounts accounts, Posts posts, int size) {
        List<Comments> ret = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ret.add(createAndPersistComments(content + "i", accounts, posts));
        }

        return ret;
    }


}
