package com.brandpark.simplepostsboard.api.comments;

import com.brandpark.simplepostsboard.api.comments.dto.CommentsListResponse;
import com.brandpark.simplepostsboard.api.comments.dto.CommentsSaveRequest;
import com.brandpark.simplepostsboard.infra.config.SessionAccounts;
import com.brandpark.simplepostsboard.modules.accounts.LoginAccounts;
import com.brandpark.simplepostsboard.modules.commnets.Comments;
import com.brandpark.simplepostsboard.modules.commnets.CommentsRepository;
import com.brandpark.simplepostsboard.modules.commnets.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postsId}/comments")
@RestController
public class CommentsApiController {

    private final CommentsRepository commentsRepository;
    private final CommentsService commentsService;

    @GetMapping
    public CommentsListResponse getAllCommentsInPosts(@PathVariable Long postsId) {
        List<Comments> allComments = commentsRepository.findAllByPostsId(postsId);

        return new CommentsListResponse(allComments);
    }

    @PostMapping
    public Long registerComments(@LoginAccounts SessionAccounts accounts, @PathVariable Long postsId, @RequestBody CommentsSaveRequest reqData) {
        return commentsService.registerComments(accounts, postsId, reqData);
    }
}
