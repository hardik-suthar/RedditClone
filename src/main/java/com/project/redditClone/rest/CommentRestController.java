package com.project.redditClone.rest;

import com.project.redditClone.entity.Comment;
import com.project.redditClone.pojo.request.PostComment;
import com.project.redditClone.pojo.request.UpdateComment;
import com.project.redditClone.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService commentService;

    @Autowired
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public Comment createComment(@RequestBody PostComment postComment , Principal principal) {
        return commentService.createComment(postComment , principal.getName());
    }

    @GetMapping("/{id}")
    public Comment getComment(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable Long id , Principal principal) {
        commentService.deleteCommentById(id , principal.getName());
        return "Comment id '" + id + "' deleted successfully";
    }

    @PutMapping
    public Comment updateComment(@RequestBody UpdateComment updatedComment , Principal principal) {
        return commentService.updateComment(updatedComment , principal.getName());
    }

    @GetMapping("/{postId}/{offSet}/{pageSize}")
    public Page<Comment> getByPostId(@PathVariable Long postId , @PathVariable int offSet , @PathVariable int pageSize) {
        return commentService.getCommentsByPostId(postId, offSet, pageSize);
    }
}
