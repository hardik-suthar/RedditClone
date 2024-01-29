package com.project.redditClone.service;

import com.project.redditClone.dao.CommunityRepository;
import com.project.redditClone.dao.PostRepository;
import com.project.redditClone.dao.UserRepository;
import com.project.redditClone.entity.Community;
import com.project.redditClone.entity.Post;
import com.project.redditClone.entity.User;
import com.project.redditClone.exception.AuthorizationException;
import com.project.redditClone.exception.NotFoundException;
import com.project.redditClone.exception.NullDataException;
import com.project.redditClone.exception.TooLongLengthException;
import com.project.redditClone.pojo.request.Postpost;
import com.project.redditClone.pojo.request.UpdatePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{

    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CommunityRepository communityRepository;

    private void cleanData(Postpost postpost) {
        if(postpost.getContent() != null) postpost.setContent(postpost.getContent().trim());
        if(postpost.getUrl() != null) postpost.setUrl(postpost.getUrl().trim());
    }

    public void checkRequiredData(Postpost postpost) {
        if( (postpost.getContent() != null) && (postpost.getContent().isEmpty()) ) postpost.setContent(null);
        if( (postpost.getUrl() != null) && (postpost.getUrl().isEmpty()) ) postpost.setUrl(null);
    }

    public void checkLength(Postpost postpost) {
        if( (postpost.getContent() != null) && (postpost.getContent().length() > 200) )
            throw new TooLongLengthException("Content length must be less than or equal to 200 characters");

        if( (postpost.getUrl() != null) && (postpost.getUrl().length() > 200) )
            throw new TooLongLengthException("Url length must be less than or equal to 200 characters");
    }

    private void cleanData(UpdatePost updatedPost) {
        if(updatedPost.getContent() != null) updatedPost.setContent(updatedPost.getContent().trim());
        if(updatedPost.getUrl() != null) updatedPost.setUrl(updatedPost.getUrl().trim());
    }

    public void checkRequiredData(UpdatePost updatedPost) {
        if( (updatedPost.getContent() != null) && (updatedPost.getContent().isEmpty()) ) updatedPost.setContent(null);
        if( (updatedPost.getUrl() != null) && (updatedPost.getUrl().isEmpty()) ) updatedPost.setUrl(null);
    }

    public void checkLength(UpdatePost updatedPost) {
        if( (updatedPost.getContent() != null) && (updatedPost.getContent().length() > 200) )
            throw new TooLongLengthException("Content length must be less than or equal to 200 characters");

        if( (updatedPost.getUrl() != null) && (updatedPost.getUrl().length() > 200) )
            throw new TooLongLengthException("Url length must be less than or equal to 200 characters");
    }

    @Override
    @Transactional
    public Post createPost(Postpost postpost , String userName) {

        Community community = communityRepository.findById(postpost.getCommunityId()).orElse(null);
        if(community == null) throw new NotFoundException("Community id '" + postpost.getCommunityId() + "' not found");

        cleanData(postpost);

        checkRequiredData(postpost);

        checkLength(postpost);

        if( (postpost.getContent() == null) && (postpost.getUrl() == null) )
            throw new NullDataException("Either content or url at least one is required");

        Post post = new Post();
        post.setContent(postpost.getContent());
        post.setUrl(postpost.getUrl());
        post.setUser(userRepository.findById(userName).orElse(null));
        post.setCommunity(community);

        return postRepository.save(post);
    }

    @Override
    public Post getPostById(Long id) {

        Post post = postRepository.findById(id).orElse(null);

        if(post == null) throw new NotFoundException("Post id '" + id + "' not found");

        return post;
    }

    @Override
    @Transactional
    public void deletePostById(Long id , String userName) {

        Post post = postRepository.findById(id).orElse(null);
        if(post == null) throw new NotFoundException("Post id '" + id + "' not found");

        if(!post.getUser().getUsername().equals(userName))
            throw new AuthorizationException("You are not authorized to delete post id '" + id + "'");

        postRepository.delete(post);
    }

    @Override
    @Transactional
    public Post updatePost(UpdatePost updatedPost , String userName) {

        Post post = postRepository.findById(updatedPost.getId()).orElse(null);
        if(post == null) throw new NotFoundException("Post id '" + updatedPost.getId() + "' not found");

        if(!post.getUser().getUsername().equals(userName))
            throw new AuthorizationException("You are not authorized to update post id '" + post.getId() + "'");

        cleanData(updatedPost);

        checkRequiredData(updatedPost);

        checkLength(updatedPost);

        if( (updatedPost.getContent() == null) && (updatedPost.getUrl() == null) )
            throw new NullDataException("Either content or url at least one is required");

        post.setContent(updatedPost.getContent());
        post.setUrl(updatedPost.getUrl());

        return postRepository.save(post);
    }

    @Override
    public Set<User> getSavedByUsers(Long postId) {

        Post post = postRepository.findById(postId).orElse(null);
        if(post == null) throw new NotFoundException("Post id '" + postId + "' not found");

        return post.getSavedByUsers();
    }

    @Override
    public Page<Post> getPostsWithPagination(int offSet, int pageSize) {
        return postRepository.findAll(PageRequest.of(offSet, pageSize));
    }

    @Override
    public Page<Post> getUserCommunityPosts(String userId , int offSet , int pageSize) {

        User user = userRepository.findById(userId).orElse(null);
        if(user == null) throw new NotFoundException("User name '" + userId + "' not found");

        Set<Community> communities = user.getJoinedCommunities();
        if(communities.isEmpty()) throw new NotFoundException("You haven't joined any communities yet please join communities to see posts");

        Set<Long> CommunityIds = communities.stream()
                                            .map(Community::getId)
                                            .collect(Collectors.toSet());

        Pageable pageable = PageRequest.of(offSet,pageSize);

        return postRepository.findByCommunityIdInOrderByCreatedOnDesc(CommunityIds,pageable);
    }

    @Override
    public Page<Post> getByCommunityId(Long communityId, int offSet, int pageSize) {

        Community community = communityRepository.findById(communityId).orElse(null);
        if(community == null) throw new NotFoundException("Community id '" + communityId + "' not found");

        Pageable pageable = PageRequest.of(offSet,pageSize, Sort.by("createdOn").descending());

        return postRepository.findByCommunityOrderByCreatedOnDesc(community,pageable);
    }
}
