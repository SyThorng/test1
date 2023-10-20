package com.kshrd.soccer_date.controller;

import com.kshrd.soccer_date.model.Comment;
import com.kshrd.soccer_date.model.Post;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.request.PostRequest;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@SecurityRequirement(name = "auth")
public class CommunityController {
    @Value("${image.url}")
    private String baseUrl;

    private static Integer currentId() {
        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUserId();
    }

    private final PostService postService;

    public CommunityController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "/posts")
    public ResponseEntity<Response<Post>> addNewPost(@RequestBody PostRequest request) {
        Post post = postService.addNewPost(request, currentId());
        if (post.getUrl().isEmpty()) {
            post.setUrl(null);
        }else {
            post.setUrl(baseUrl + post.getUrl());
        }
        post.setProfile_image(baseUrl+post.getProfile_image());

        Response<Post> response = Response.<Post>builder()
                .message("Post success..!!")
                .payload(post)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value = "posts/{id}")
    public ResponseEntity<Response<Post>> editPost(@RequestBody PostRequest request, @PathVariable(value = "id") Integer post_id) {
        Post post = postService.editPost(request, post_id, currentId());
        if (post.getUrl().isEmpty()) {
            post.setUrl(null);
        }else {
            post.setUrl(baseUrl + post.getUrl());
        }
        post.setProfile_image(baseUrl + post.getProfile_image());
        Response<Post> response = Response.<Post>builder()
                .message("Edited Post success..!!")
                .payload(post)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "posts/{id}")
    public ResponseEntity<Response<Post>> getPostById(@PathVariable(value = "id") Integer post_id) {
        Post post = postService.getPostById(post_id);
        if (post.getUrl().isEmpty()) {
            post.setUrl(null);
        }else {
            post.setUrl(baseUrl + post.getUrl());
        }
        post.setProfile_image(post.getProfile_image());
        Response<Post> response = Response.<Post>builder()
                .message("Fetch Post id " + post_id + " successfully!!")
                .payload(post)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/posts")
    public ResponseEntity<Response<List<Post>>> getAllPost(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "3") Integer pageSize) {
        List<Post> posts = postService.getAllPosts(pageNo, pageSize,currentId());
        for (Post post : posts) {
            if (post.getUrl().isEmpty()) {
                post.setUrl(null);

            }else {
                post.setUrl(baseUrl + post.getUrl());
            }

            post.setProfile_image(baseUrl + post.getProfile_image());
        }
        Response<List<Post>> response = Response.<List<Post>>builder()
                .message("Fetch all posts successfully..!!")
                .payload(posts)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = "posts/{id}")
    public ResponseEntity<Response<Post>> deletePost(@PathVariable(value = "id") Integer post_id) {
        postService.deletePost(post_id, currentId());
        Response<Post> response = Response.<Post>builder()
                .message("This Post is deleted success..!!")
                .payload(null)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

}
