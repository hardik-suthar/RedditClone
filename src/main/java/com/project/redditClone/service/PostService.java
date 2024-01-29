package com.project.redditClone.service;

import com.project.redditClone.entity.Post;
import com.project.redditClone.entity.User;
import com.project.redditClone.pojo.request.Postpost;
import com.project.redditClone.pojo.request.UpdatePost;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface PostService {

    Post createPost(Postpost postpost , String UserName);

    Post getPostById(Long id);

    void deletePostById(Long id , String userName);

    Post updatePost(UpdatePost updatedPost , String userName);

    Set<User> getSavedByUsers(Long postId);

    Page<Post> getPostsWithPagination(int offSet , int pageSize);

    Page<Post> getUserCommunityPosts(String userId , int offSet , int pageSize);

    Page<Post> getByCommunityId(Long communityId , int offSet , int pageSize);
}
