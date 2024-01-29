package com.project.redditClone.service;

import com.project.redditClone.dao.CommentRepository;
import com.project.redditClone.dao.CommentVoteRepository;
import com.project.redditClone.dao.UserRepository;
import com.project.redditClone.entity.*;
import com.project.redditClone.exception.NotFoundException;
import com.project.redditClone.pojo.request.PostCommentVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentVoteServiceImpl implements CommentVoteService{

    private final CommentVoteRepository commentVoteRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentVoteServiceImpl(CommentVoteRepository commentVoteRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.commentVoteRepository = commentVoteRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CommentVote createCommentVote(PostCommentVote postCommentVote , String userName) {

        Comment comment = commentRepository.findById(postCommentVote.getCommentId()).orElse(null);
        if(comment == null) throw new NotFoundException("Comment id '" + postCommentVote.getCommentId() + "' not found");

        CommentVote commentVote = commentVoteRepository.findByCommentIdAndUserId(postCommentVote.getCommentId(), userName);

        if(commentVote != null) {

            if( (commentVote.isVoteType()) && (postCommentVote.isVoteType()) ) {
                comment.setTotalVotes(comment.getTotalVotes() - 1);
                commentRepository.save(comment);
                commentVoteRepository.delete(commentVote);
            }

            else if( (!commentVote.isVoteType()) && (!postCommentVote.isVoteType()) ) {
                comment.setTotalVotes(comment.getTotalVotes() + 1);
                commentRepository.save(comment);
                commentVoteRepository.delete(commentVote);
            }

            else if(commentVote.isVoteType()) {
                comment.setTotalVotes(comment.getTotalVotes() - 2);
                commentRepository.save(comment);
                commentVote.setVoteType(false);
                commentVoteRepository.save(commentVote);
            }

            else {
                comment.setTotalVotes(comment.getTotalVotes() + 2);
                commentRepository.save(comment);
                commentVote.setVoteType(true);
                commentVoteRepository.save(commentVote);
            }
        }

        else {

            if (postCommentVote.isVoteType()) {
                comment.setTotalVotes(comment.getTotalVotes() + 1);
            } else {
                comment.setTotalVotes(comment.getTotalVotes() - 1);
            }

            commentVote = new CommentVote();
            commentVote.setVoteType(postCommentVote.isVoteType());
            commentVote.setComment(comment);
            commentVote.setUser(userRepository.findById(userName).orElse(null));
            commentRepository.save(comment);
            commentVoteRepository.save(commentVote);
        }

        return commentVote;
    }
}
