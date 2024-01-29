package com.project.redditClone.rest;

import com.project.redditClone.entity.CommentVote;
import com.project.redditClone.pojo.request.PostCommentVote;
import com.project.redditClone.service.CommentVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/commentVotes")
public class CommentVoteRestController {

    private final CommentVoteService commentVoteService;

    @Autowired
    public CommentVoteRestController(CommentVoteService commentVoteService) {
        this.commentVoteService = commentVoteService;
    }

    @PostMapping
    public CommentVote createVote(@RequestBody PostCommentVote postCommentVote , Principal principal) {
        return commentVoteService.createCommentVote(postCommentVote , principal.getName());
    }
}
