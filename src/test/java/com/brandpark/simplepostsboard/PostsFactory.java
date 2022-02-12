package com.brandpark.simplepostsboard;

import com.brandpark.simplepostsboard.modules.accounts.Accounts;
import com.brandpark.simplepostsboard.modules.posts.Posts;
import com.brandpark.simplepostsboard.modules.posts.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Profile("test")
@Component
public class PostsFactory {

    @Autowired PostsRepository postsRepository;

    public List<Posts> createAndPersistPostsList(String title, String content, Accounts accounts, int size) {
        List<Posts> result = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            result.add(createAndPersistPosts(title + "i", content + "i", accounts));
        }

        return result;
    }

    public Posts createAndPersistPosts(String title, String content, Accounts accounts) {
        return postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .accounts(accounts)
                .build());
    }

    public Posts createAndPersistPosts(String title, String content, Accounts accounts, int viewCount) {
        return postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .accounts(accounts)
                .viewCount(viewCount)
                .build());
    }
}
