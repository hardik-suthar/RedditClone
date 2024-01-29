package com.project.redditClone.service;

import com.project.redditClone.entity.Comment;
import com.project.redditClone.pojo.request.PostComment;
import com.project.redditClone.pojo.request.UpdateComment;
import org.springframework.data.domain.Page;

public interface CommentService {

    Comment createComment(PostComment postComment , String userName);

    Comment getCommentById(Long id);

    void deleteCommentById(Long id , String userName);

    Comment updateComment(UpdateComment updatedComment , String userName);

    Page<Comment> getCommentsByPostId(Long postId , int offSet , int pageSize);
}
