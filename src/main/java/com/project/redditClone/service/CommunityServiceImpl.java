package com.project.redditClone.service;

import com.project.redditClone.dao.CommunityRepository;
import com.project.redditClone.dao.UserRepository;
import com.project.redditClone.entity.Community;
import com.project.redditClone.entity.User;
import com.project.redditClone.exception.*;
import com.project.redditClone.pojo.request.PostCommunity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class CommunityServiceImpl implements CommunityService{

    @Autowired private UserRepository userRepository;
    @Autowired private CommunityRepository communityRepository;

    private void cleanData(PostCommunity postCommunity) {
        if(postCommunity.getCommunityName() != null) postCommunity.setCommunityName(postCommunity.getCommunityName().trim());
    }

    private void checkRequiredData(PostCommunity postCommunity) {
        if(postCommunity.getCommunityName().isEmpty()) throw new NullDataException("Community name is required");
    }

    private void lengthValidation(PostCommunity postCommunity) {
        if(postCommunity.getCommunityName().length() > 50) throw new TooLongLengthException("Community name must be less than or equal to 50 characters");
    }

    @Override
    @Transactional
    public Community createCommunity(PostCommunity postCommunity , String userName) {

        cleanData(postCommunity);

        checkRequiredData(postCommunity);

        lengthValidation(postCommunity);

        if(communityRepository.findByName(postCommunity.getCommunityName()) != null) throw new DuplicateException("Community already exists");

        Community community = new Community();
        community.setName(postCommunity.getCommunityName());
        community.setCreatedBy(userRepository.findById(userName).orElse(null));

        return communityRepository.save(community);
    }

    @Override
    public Community getCommunityByName(String name) {

        Community community = communityRepository.findByName(name.trim());

        if(community == null) throw new NotFoundException("Community not found");

        return community;
    }

    @Override
    @Transactional
    public void deleteCommunityByName(String name , String userName) {

        Community community = communityRepository.findByName(name.trim());
        if(community == null) throw new NotFoundException("Community not found");

        if(!community.getCreatedBy().getUsername().equals(userName))
            throw new AuthorizationException("You are not authorized to delete community '" + name + "'");

        communityRepository.delete(community);
    }

    @Override
    public Set<User> getJoinedUsers(Long id) {

        Community community = communityRepository.findById(id).orElse(null);
        if(community == null) throw new NotFoundException("Community id '" + id + "' not found");

        return community.getJoinedByUsers();
    }
}
