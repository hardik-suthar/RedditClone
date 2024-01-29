package com.project.redditClone.service;

import com.project.redditClone.dao.PostRepository;
import com.project.redditClone.dao.PostVoteRepository;
import com.project.redditClone.dao.UserRepository;
import com.project.redditClone.entity.Post;
import com.project.redditClone.entity.PostVote;
import com.project.redditClone.entity.User;
import com.project.redditClone.exception.NotFoundException;
import com.project.redditClone.pojo.request.PostpostVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostVoteServiceImpl implements PostVoteService{

    @Autowired private PostVoteRepository postVoteRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;

    @Override
    @Transactional
    public PostVote createPostVote(PostpostVote postpostVote , String userName) {

        Post post = postRepository.findById(postpostVote.getPostId()).orElse(null);
        if(post == null) throw new NotFoundException("Post id '" + postpostVote.getPostId() + "' not found");

        PostVote postVote = postVoteRepository.findByPostIdAndUserId(postpostVote.getPostId(), userName);

        if(postVote != null) {

            if( (postVote.isVoteType()) && (postpostVote.isVoteType()) ) {
                post.setTotalVotes(post.getTotalVotes() - 1);
                postRepository.save(post);
                postVoteRepository.delete(postVote);
            }

            else if( (!postVote.isVoteType()) && (!postpostVote.isVoteType()) ) {
                post.setTotalVotes(post.getTotalVotes() + 1);
                postRepository.save(post);
                postVoteRepository.delete(postVote);
            }

            else if(postVote.isVoteType()) {
                post.setTotalVotes(post.getTotalVotes() - 2);
                postRepository.save(post);
                postVote.setVoteType(false);
                postVoteRepository.save(postVote);
            }

            else {
                post.setTotalVotes(post.getTotalVotes() + 2);
                postRepository.save(post);
                postVote.setVoteType(true);
                postVoteRepository.save(postVote);
            }
        }

        else {

            if (postpostVote.isVoteType()) {
                post.setTotalVotes(post.getTotalVotes() + 1);
            } else {
                post.setTotalVotes(post.getTotalVotes() - 1);
            }

            postVote = new PostVote();
            postVote.setVoteType(postpostVote.isVoteType());
            postVote.setPost(post);
            postVote.setUser(userRepository.findById(userName).orElse(null));
            postRepository.save(post);
            postVoteRepository.save(postVote);
        }

        return postVote;
    }
}
