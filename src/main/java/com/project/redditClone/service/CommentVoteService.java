package com.project.redditClone.service;

import com.project.redditClone.entity.CommentVote;
import com.project.redditClone.pojo.request.PostCommentVote;

public interface CommentVoteService {

    CommentVote createCommentVote(PostCommentVote postCommentVote , String userName);
}
