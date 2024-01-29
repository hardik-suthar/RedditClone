package com.project.redditClone.service;

import com.project.redditClone.entity.PostVote;
import com.project.redditClone.pojo.request.PostpostVote;

public interface PostVoteService {

    PostVote createPostVote(PostpostVote postpostVote , String userName);
}
