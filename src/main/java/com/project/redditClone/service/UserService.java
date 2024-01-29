package com.project.redditClone.service;

import com.project.redditClone.entity.Community;
import com.project.redditClone.entity.Post;
import com.project.redditClone.entity.User;
import com.project.redditClone.pojo.request.*;

import java.util.Set;

public interface UserService {

    User createUser(PostUser postUser);

    User getUserByUserName(String userName);

    void deleteUserByUserName(String userName , String loggedInUser);

    User updateUser(UpdateUser updatedUser , String userName);

    String savePost(PostSavePost postSavePost , String userName);

    Set<Post> getSavedPost(String userId);

    String joinCommunity(PostJoinCommunity postJoinCommunity , String userName);

    Set<Community> getJoinedCommunities(String userId);

    void updatePassword(UpdatePassword updatePassword , String userName);
}
