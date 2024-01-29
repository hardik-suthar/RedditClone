package com.project.redditClone.dao;

import com.project.redditClone.entity.CommentVote;
import com.project.redditClone.entity.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentVoteRepository extends JpaRepository<CommentVote,Long> {

    @Query(value = "SELECT * FROM comment_vote WHERE comment_id = :commentId AND user_id = :userId",nativeQuery = true)
    CommentVote findByCommentIdAndUserId(@Param("commentId") Long commentId , @Param("userId") String userId);

    @Query(value = "SELECT * FROM comment_vote WHERE user_id = :userId" , nativeQuery = true)
    List<CommentVote> findByUserId(String userId);
}
