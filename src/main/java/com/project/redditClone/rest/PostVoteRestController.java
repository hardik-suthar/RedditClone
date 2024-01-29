package com.project.redditClone.rest;

import com.project.redditClone.entity.PostVote;
import com.project.redditClone.pojo.request.PostpostVote;
import com.project.redditClone.service.PostVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/postVotes")
public class PostVoteRestController {

    private final PostVoteService postVoteService;

    @Autowired
    public PostVoteRestController(PostVoteService postVoteService) {
        this.postVoteService = postVoteService;
    }

    @PostMapping
    public PostVote createVote(@RequestBody PostpostVote postpostVote , Principal principal) {
        return postVoteService.createPostVote(postpostVote , principal.getName());
    }
}
