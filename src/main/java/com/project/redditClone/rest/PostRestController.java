package com.project.redditClone.rest;

import com.project.redditClone.entity.Post;
import com.project.redditClone.entity.User;
import com.project.redditClone.pojo.request.Postpost;
import com.project.redditClone.pojo.request.UpdatePost;
import com.project.redditClone.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;

    @Autowired
    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public Post createPost(@RequestBody Postpost postpost , Principal principal) {
        return postService.createPost(postpost , principal.getName());
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable Long id , Principal principal) {
        postService.deletePostById(id , principal.getName());
        return "Post id '" + id + "' deleted successfully";
    }

    @PutMapping
    public Post updatePost(@RequestBody UpdatePost updatedPost , Principal principal) {
        return postService.updatePost(updatedPost , principal.getName());
    }

    @GetMapping("/savedByUsers/{id}")
    public Set<User> getSavedByUsers(@PathVariable Long id) {
        return postService.getSavedByUsers(id);
    }

    @GetMapping("/{offSet}/{pageSize}")
    public Page<Post> getPostsWithPagination(@PathVariable int offSet , @PathVariable int pageSize) {
        return postService.getPostsWithPagination(offSet, pageSize);
    }

    @GetMapping("/userCommunityPosts/{userId}/{offSet}/{pageSize}")
    public Page<Post> getUserCommunityPosts(@PathVariable String userId , @PathVariable int offSet , @PathVariable int pageSize) {
        return postService.getUserCommunityPosts(userId, offSet, pageSize);
    }

    @GetMapping("/communityPosts/{communityId}/{offSet}/{pageSize}")
    public Page<Post> getByCommunityId(@PathVariable Long communityId , @PathVariable int offSet , @PathVariable int pageSize) {
        return postService.getByCommunityId(communityId, offSet, pageSize);
    }
}
