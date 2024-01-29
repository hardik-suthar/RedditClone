package com.project.redditClone.dao;

import com.project.redditClone.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community,Long> {
    Community findByName(String name);
}
