package com.kshrd.soccer_date.controller;

import com.kshrd.soccer_date.model.Comment;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.request.CommentRequest;
import com.kshrd.soccer_date.model.request.UpdateCommentRequest;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@SecurityRequirement(name = "auth")
public class CommentController {
    private final CommentService commentService;
    @Value("${image.url}")
    private String baseUrl;
    private static Integer currentId() {
        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUserId();
    }

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public ResponseEntity<Response<Comment>> commentOnPost(@RequestBody CommentRequest request) {
        Comment comment=commentService.commentOnPost(request, currentId());
        comment.setProfile(baseUrl+comment.getProfile());
        Response<Comment> response = Response.<Comment>builder()
                .message("Comment has been successfully..!!")
                .payload(comment)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }


    @PutMapping("/comment")
    public ResponseEntity<Response<Comment>> editComment(@RequestBody UpdateCommentRequest request) {
        Comment comment=commentService.editComment(request, currentId());
        comment.setProfile(baseUrl+comment.getProfile());
        Response<Comment> response = Response.<Comment>builder()
                .message("Comment has been updated success..!!")
                .payload(comment)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Response<Comment>> editComment(@PathVariable("id") Integer id) {
        commentService.removeComment(id, currentId());
        Response<Comment> response = Response.<Comment>builder()
                .message("Comment has been deleted success..!!")
                .payload(null)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/comment/{post_id}")
    public ResponseEntity<Response<List<Comment>>> getCommentByPostId(@PathVariable("post_id") Integer post_id) {
        List<Comment>comments=commentService.getCommentByPostId(post_id);
        for (Comment comment:comments){
            comment.setProfile(baseUrl+comment.getProfile());
        }
        Response<List<Comment>> response = Response.<List<Comment>>builder()
                .message("Comments by post ID have been fetched success..!!")
                .payload(comments)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
}