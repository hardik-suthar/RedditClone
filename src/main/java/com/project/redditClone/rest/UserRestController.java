package com.project.redditClone.rest;

import com.project.redditClone.entity.Community;
import com.project.redditClone.entity.Post;
import com.project.redditClone.entity.User;
import com.project.redditClone.pojo.request.*;
import com.project.redditClone.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public User createUser(@RequestBody PostUser postUser) {
        return userService.createUser(postUser);
    }

    @GetMapping("/{userName}")
    public User findUser(@PathVariable String userName) {
        return userService.getUserByUserName(userName);
    }

    @DeleteMapping("/{userName}")
    public String deleteUser(@PathVariable String userName , Principal principal) {
        userService.deleteUserByUserName(userName , principal.getName());
        return "User deleted successfully";
    }

    @PutMapping
    public User updateUser(@RequestBody UpdateUser updatedUser , Principal principal) {
        return userService.updateUser(updatedUser , principal.getName());
    }

    @PostMapping("/savePost")
    public String savePost(@RequestBody PostSavePost postSavePost , Principal principal) {
        return userService.savePost(postSavePost , principal.getName());
    }

    @GetMapping("/savedPost/{userName}")
    public Set<Post> getSavedPosts(@PathVariable String userName) {
        return userService.getSavedPost(userName);
    }

    @PostMapping("/joinCommunity")
    public String joinCommunity(@RequestBody PostJoinCommunity postJoinCommunity , Principal principal) {
        return userService.joinCommunity(postJoinCommunity , principal.getName());
    }

    @GetMapping("/joinedCommunities/{userName}")
    public Set<Community> getJoinedCommunities(@PathVariable String userName) {
        return userService.getJoinedCommunities(userName);
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@RequestBody UpdatePassword updatePassword , Principal principal) {
        userService.updatePassword(updatePassword , principal.getName());
        return "Password updated successfully";
    }
}
