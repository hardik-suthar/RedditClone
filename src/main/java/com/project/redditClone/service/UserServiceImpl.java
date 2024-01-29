package com.project.redditClone.service;

import com.project.redditClone.dao.*;
import com.project.redditClone.entity.*;
import com.project.redditClone.exception.*;
import com.project.redditClone.pojo.request.*;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private PostVoteRepository postVoteRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private CommentVoteRepository commentVoteRepository;
    @Autowired private CommunityRepository communityRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private void checkRequiredData(PostUser postUser) {
        if(postUser.getUserName().isEmpty()) throw new NullDataException("Username is required");
        if(postUser.getFirstName().isEmpty()) throw new NullDataException("Firstname is required");
        if(postUser.getLastName().isEmpty()) throw new NullDataException("Lastname is required");
        if(postUser.getEmail().isEmpty()) throw new NullDataException("Email is required");
        if(postUser.getPassword().isEmpty()) throw new NullDataException("Password is required");
        if( (postUser.getBio() != null) && (postUser.getBio().isEmpty()) ) postUser.setBio(null);
    }

    private void checkRequiredData(UpdateUser updatedUser) {
        if(updatedUser.getFirstName().isEmpty()) throw new NullDataException("Firstname is required");
        if(updatedUser.getLastName().isEmpty()) throw new NullDataException("Lastname is required");
        if(updatedUser.getEmail().isEmpty()) throw new NullDataException("Email is required");
        if( (updatedUser.getBio() != null) && (updatedUser.getBio().isEmpty()) ) updatedUser.setBio(null);
    }

    private void cleanData(PostUser postUser) {
        if(postUser.getUserName() != null) postUser.setUserName(postUser.getUserName().trim());
        if(postUser.getFirstName()!= null) postUser.setFirstName(postUser.getFirstName().trim());
        if(postUser.getLastName() != null) postUser.setLastName(postUser.getLastName().trim());
        if(postUser.getEmail() != null) postUser.setEmail(postUser.getEmail().trim());
        if(postUser.getPassword() != null) postUser.setPassword(postUser.getPassword().trim());
        if(postUser.getBio() != null) postUser.setBio(postUser.getBio().trim());
    }

    private void cleanData(UpdateUser updatedUser) {
        if(updatedUser.getFirstName()!= null) updatedUser.setFirstName(updatedUser.getFirstName().trim());
        if(updatedUser.getLastName() != null) updatedUser.setLastName(updatedUser.getLastName().trim());
        if(updatedUser.getEmail() != null) updatedUser.setEmail(updatedUser.getEmail().trim());
        if(updatedUser.getBio() != null) updatedUser.setBio(updatedUser.getBio().trim());
    }

    private boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    private void lengthValidation(PostUser postUser) {
        if(postUser.getUserName().length() > 15) throw new TooLongLengthException("User name must be less than or equal to 15 characters");
        if(postUser.getFirstName().length() > 20) throw new TooLongLengthException("First name must be less than or equal to 20 characters");
        if(postUser.getLastName().length() > 20) throw new TooLongLengthException("Last name must be less than or equal to 20 characters");
        if(postUser.getEmail().length() > 100) throw new TooLongLengthException("Email must be less than or equal to 100 characters");
        if(postUser.getPassword().length() > 50) throw new TooLongLengthException("Password must be less than or equal to 50 characters");
        if( (postUser.getBio() != null) && (postUser.getBio().length() > 200) )
            throw new TooLongLengthException("Bio must be less than or equal to 200 characters");
    }

    private void lengthValidation(UpdateUser updatedUser) {
        if(updatedUser.getFirstName().length() > 20) throw new TooLongLengthException("First name must be less than or equal to 20 characters");
        if(updatedUser.getLastName().length() > 20) throw new TooLongLengthException("Last name must be less than or equal to 20 characters");
        if(updatedUser.getEmail().length() > 100) throw new TooLongLengthException("Email must be less than or equal to 100 characters");
        if( (updatedUser.getBio() != null) && (updatedUser.getBio().length() > 200) )
            throw new TooLongLengthException("Bio must be less than or equal to 200 characters");
    }

    @Override
    @Transactional
    public User createUser(PostUser postUser) {

        cleanData(postUser);

        checkRequiredData(postUser);

        lengthValidation(postUser);

        if(userRepository.existsByUserName(postUser.getUserName())) throw new DuplicateException("User name already taken");

        if(userRepository.existsByEmail(postUser.getEmail())) throw new DuplicateException("Email address is already in use");

        if(!isValidEmail(postUser.getEmail())) throw new InvalidException("Invalid email");

        User user = new User();
        user.setUserName(postUser.getUserName());
        user.setPassword(passwordEncoder.encode(postUser.getPassword()));
        user.setFirstName(postUser.getFirstName());
        user.setLastName(postUser.getLastName());
        user.setEmail(postUser.getEmail());
        user.setBio(postUser.getBio());

        return userRepository.save(user);
    }

    @Override
    public User getUserByUserName(String userName) {

        User user = userRepository.findById(userName.trim()).orElse(null);

        if(user == null) throw new NotFoundException("User not found");

        return user;
    }

    @Override
    @Transactional
    public void deleteUserByUserName(String userName , String loggedInUser) {

        if(!userName.trim().equals(loggedInUser))
            throw new AuthorizationException("You are not authorized to delete user '" + userName + "'");

        User user = userRepository.findById(userName.trim()).orElse(null);

        List<PostVote> postVotes = postVoteRepository.findByUserId(userName);

        for(PostVote postVote : postVotes) {

            Post post = postVote.getPost();

            if(postVote.isVoteType()) post.setTotalVotes(post.getTotalVotes() - 1);
            else post.setTotalVotes(post.getTotalVotes() + 1);

            postRepository.save(post);
        }

        List<CommentVote> commentVotes = commentVoteRepository.findByUserId(userName);

        for(CommentVote commentVote : commentVotes) {

            Comment comment = commentVote.getComment();

            if(commentVote.isVoteType()) comment.setTotalVotes(comment.getTotalVotes() - 1);
            else comment.setTotalVotes(comment.getTotalVotes() + 1);

            commentRepository.save(comment);
        }

        userRepository.delete(user);
    }

    @Override
    @Transactional
    public User updateUser(UpdateUser updatedUser , String userName) {

        User user = userRepository.findById(userName).orElse(null);

        cleanData(updatedUser);

        checkRequiredData(updatedUser);

        lengthValidation(updatedUser);

        if(!user.getEmail().equals(updatedUser.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail()))
                throw new DuplicateException("Email address is already in use");

            if (!isValidEmail(updatedUser.getEmail()))
                throw new InvalidException("Invalid email");

            user.setEmail(updatedUser.getEmail());
        }

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setBio(updatedUser.getBio());

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public String savePost(PostSavePost postSavePost , String userName) {

        User user = userRepository.findById(userName).orElse(null);

        Post post = postRepository.findById(postSavePost.getPostId()).orElse(null);
        if(post == null) throw new NotFoundException("Post id '" + postSavePost.getPostId() + "' not found");

        String message;
        if(user.getSavedPosts().contains(post)) {
            user.getSavedPosts().remove(post);
            post.getSavedByUsers().remove(user);
            message = "Post id '" + post.getId() + "' unsaved";
        }
        else {
            user.getSavedPosts().add(post);
            post.getSavedByUsers().add(user);
            message = "Post id '" + post.getId() + "' saved";
        }

        userRepository.save(user);
        postRepository.save(post);

        return message;
    }

    @Override
    public Set<Post> getSavedPost(String userId) {

        User user = userRepository.findById(userId.trim()).orElse(null);
        if(user == null) throw new NotFoundException("User id '" + userId + "' not found");

        return user.getSavedPosts();
    }

    @Override
    @Transactional
    public String joinCommunity(PostJoinCommunity postJoinCommunity , String userName) {

        User user = userRepository.findById(userName).orElse(null);

        Community community = communityRepository.findById(postJoinCommunity.getCommunityId()).orElse(null);
        if(community == null) throw new NotFoundException("Community id '" + postJoinCommunity.getCommunityId() + "' not found");

        if(community.getCreatedBy().equals(user)) throw new CommunityOperationException("You are the creator you can neither join or leave community");

        String message;
        if(user.getJoinedCommunities().contains(community)) {
            user.getJoinedCommunities().remove(community);
            community.getJoinedByUsers().remove(user);
            message = "You have left the community id '" + community.getId() + "'";
        }
        else {
            user.getJoinedCommunities().add(community);
            community.getJoinedByUsers().add(user);
            message = "Community id '" + community.getId() + "' joined";
        }

        userRepository.save(user);
        communityRepository.save(community);

        return message;
    }

    @Override
    public Set<Community> getJoinedCommunities(String userId) {

        User user = userRepository.findById(userId.trim()).orElse(null);
        if(user == null) throw new NotFoundException("User id '" + userId + "' not found");

        return user.getJoinedCommunities();
    }

    @Override
    @Transactional
    public void updatePassword(UpdatePassword updatePassword , String userName) {

        User user = userRepository.findById(userName).orElse(null);

        if(!passwordEncoder.matches(updatePassword.getOldPassword(), user.getPassword()))
            throw new InvalidException("Current password is invalid");

        String newPassword = updatePassword.getNewPassword();
        if( (newPassword == null) || (newPassword.trim().isEmpty()) ) throw new NullDataException("Password is required");
        if(newPassword.length() > 50) throw new TooLongLengthException("Password must be less than or equal to 50 characters");

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
