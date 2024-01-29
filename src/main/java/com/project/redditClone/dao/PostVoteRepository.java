package com.project.redditClone.dao;

import com.project.redditClone.entity.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Long> {

    @Query(value = "SELECT * FROM post_vote WHERE post_id = :postId AND user_id = :userId",nativeQuery = true)
    PostVote findByPostIdAndUserId(@Param("postId") Long postId , @Param("userId") String userId);

    @Query(value = "SELECT * FROM post_vote WHERE user_id = :userId" , nativeQuery = true)
    List<PostVote> findByUserId(String userId);
}
