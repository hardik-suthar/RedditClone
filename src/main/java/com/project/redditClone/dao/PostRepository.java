package com.project.redditClone.dao;

import com.project.redditClone.entity.Community;
import com.project.redditClone.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    @Query(value = "SELECT * FROM post WHERE id = :id AND user_id = :userId",nativeQuery = true)
    Post findByPostIdAndUserId(@Param("id") Long id , @Param("userId") String userId);

    Page<Post> findByCommunityIdInOrderByCreatedOnDesc(Set<Long> CommunityIds , Pageable pageable);

    Page<Post> findByCommunityOrderByCreatedOnDesc(Community community, Pageable pageable);
}
