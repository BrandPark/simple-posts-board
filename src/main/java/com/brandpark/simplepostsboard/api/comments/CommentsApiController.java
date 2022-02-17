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

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postsId}/comments")
@RestController
public class CommentsApiController {

    private final CommentsRepository commentsRepository;
    private final CommentsService commentsService;

    @GetMapping
    public CommentsListResponse getAllCommentsInPosts(@LoginAccounts SessionAccounts loginAccounts, @PathVariable Long postsId) {

        List<Comments> allComments = null;

        if (loginAccounts == null) {
            allComments = commentsRepository.findAllByPostsId(postsId);
        } else {
            allComments = commentsRepository.findAllNotBlockedCommentsByPostsId(loginAccounts.getId(), postsId);
        }

        return new CommentsListResponse(allComments);
    }

    @PostMapping
    public Long registerComments(@LoginAccounts SessionAccounts accounts, @PathVariable Long postsId, @Valid @RequestBody CommentsSaveRequest reqData) {
        return commentsService.registerComments(accounts, postsId, reqData);
    }
}
