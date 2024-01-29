package com.project.redditClone.service;

import com.project.redditClone.entity.Community;
import com.project.redditClone.entity.User;
import com.project.redditClone.pojo.request.PostCommunity;

import java.util.Set;

public interface CommunityService {

    Community createCommunity(PostCommunity postCommunity , String UserName);

    Community getCommunityByName(String name);

    void deleteCommunityByName(String name , String userName);

    Set<User> getJoinedUsers(Long id);
}
