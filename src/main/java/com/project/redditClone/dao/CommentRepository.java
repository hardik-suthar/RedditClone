package com.project.redditClone.dao;

import com.project.redditClone.entity.Comment;
import com.project.redditClone.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query(value = "SELECT * FROM comment WHERE id = :id AND user_id = :userId",nativeQuery = true)
    Comment findByCommentIdAndUserId(@Param("id") Long id , @Param("userId") String userId);

    Page<Comment> findByPostOrderByCreatedOnDesc(Post post , Pageable pageable);
}
