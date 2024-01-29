package com.project.redditClone.rest;

import com.project.redditClone.entity.Community;
import com.project.redditClone.entity.User;
import com.project.redditClone.pojo.request.PostCommunity;
import com.project.redditClone.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/communities")
public class CommunityRestController {

    private final CommunityService communityService;

    @Autowired
    public CommunityRestController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @PostMapping()
    public Community createCommunity(@RequestBody PostCommunity postCommunity , Principal principal) {
        return communityService.createCommunity(postCommunity , principal.getName());
    }

    @GetMapping("/{community_name}")
    public Community findCommunity(@PathVariable String community_name) {
        return communityService.getCommunityByName(community_name);
    }

    @DeleteMapping("/{community_name}")
    public String deleteCommunity(@PathVariable String community_name , Principal principal) {
        communityService.deleteCommunityByName(community_name , principal.getName());
        return "Community '" + community_name + "' deleted successfully";
    }

    @GetMapping("/joinedUsers/{id}")
    public Set<User> getJoinedUsers(@PathVariable Long id) {
        return communityService.getJoinedUsers(id);
    }
}
