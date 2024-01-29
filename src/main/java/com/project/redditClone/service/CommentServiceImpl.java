package com.project.redditClone.service;

import com.project.redditClone.dao.CommentRepository;
import com.project.redditClone.dao.PostRepository;
import com.project.redditClone.dao.UserRepository;
import com.project.redditClone.entity.Comment;
import com.project.redditClone.entity.Post;
import com.project.redditClone.entity.User;
import com.project.redditClone.exception.AuthorizationException;
import com.project.redditClone.exception.NotFoundException;
import com.project.redditClone.exception.NullDataException;
import com.project.redditClone.exception.TooLongLengthException;
import com.project.redditClone.pojo.request.PostComment;
import com.project.redditClone.pojo.request.UpdateComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired private CommentRepository commentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;

    @Override
    @Transactional
    public Comment createComment(PostComment postComment , String userName) {

        Post post = postRepository.findById(postComment.getPostId()).orElse(null);
        if(post == null) throw new NotFoundException("Post id '" + postComment.getPostId() + "' not found");

        String content = postComment.getContent().trim();
        if(content.isEmpty()) content = null;
        if(content == null) throw new NullDataException("Content is required");
        if(content.length() > 200) throw new TooLongLengthException("Content length must be less than or equal to 200 characters");

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(userRepository.findById(userName).orElse(null));
        comment.setPost(post);

        return commentRepository.save(comment);
    }

    @Override
    public Comment getCommentById(Long id) {

        Comment comment = commentRepository.findById(id).orElse(null);

        if(comment == null) throw new NotFoundException("Comment id '" + id + "' not found");

        return comment;
    }

    @Override
    @Transactional
    public void deleteCommentById(Long id , String userName) {

        Comment comment = commentRepository.findById(id).orElse(null);
        if(comment == null) throw new NotFoundException("Comment id '" + id + "' not found");

        if(!comment.getUser().getUsername().equals(userName))
            throw new AuthorizationException("You are not authorized to delete comment id '" + id + "'");

        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public Comment updateComment(UpdateComment updatedComment , String userName) {

        Comment comment = commentRepository.findById(updatedComment.getId()).orElse(null);
        if(comment == null) throw new NotFoundException("Comment id '" + updatedComment.getId() + "' not found");

        if(!comment.getUser().getUsername().equals(userName))
            throw new AuthorizationException("You are not authorized to update comment id '" + comment.getId() + "'");

        String content = updatedComment.getContent();
        if(content == null || content.trim().isEmpty()) throw new NullDataException("Content is required");
        if(content.length() > 200) throw new TooLongLengthException("Content length must be less than or equal to 200 characters");

        comment.setContent(content);

        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> getCommentsByPostId(Long postId , int offSet , int pageSize) {

        Post post = postRepository.findById(postId).orElse(null);
        if(post == null) throw new NotFoundException("Post id '" + postId + "' not found");

        Pageable pageable = PageRequest.of(offSet,pageSize, Sort.by("createdOn").descending());

        return commentRepository.findByPostOrderByCreatedOnDesc(post,pageable);
    }
}
