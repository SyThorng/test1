package com.kshrd.soccer_date.controller;

import com.kshrd.soccer_date.model.CommentMatch;
import com.kshrd.soccer_date.model.request.CommentMatchRequest;
import com.kshrd.soccer_date.model.request.CommentMatchRequestUpdate;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.CommentMatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "auth")
@RequestMapping("/api/v1/comment-match")
public class CommentMatchController {

    private final CommentMatchService commentMatchService;

    public CommentMatchController(CommentMatchService commentMatchService) {
        this.commentMatchService = commentMatchService;
    }
    String getUsernameOfCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        return username;
    }
    @PostMapping("")
    public ResponseEntity <Response<CommentMatch>> insertComment(
            @RequestBody CommentMatchRequest commentMatchRequest
            ) {
            Response<CommentMatch> response = Response.<CommentMatch>builder()
                    .message("Post Comment Success..!!")
                    .payload(commentMatchService.insertComment(commentMatchRequest,getUsernameOfCurrentUser()))
                    .status(200)
                    .build();
            return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-all-match-comment")
    public ResponseEntity<Response<List<CommentMatch>>> getALlCommentMatch() {
        Response<List<CommentMatch>> response = Response.<List<CommentMatch>>builder()
                .message("get all Comment match Success..!!")
                .payload(commentMatchService.getALlCommentMatch())
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Response<CommentMatch>> searchCommentMatchById(
            @PathVariable Integer  commentId
    ) {
        Response<CommentMatch> response = Response.<CommentMatch>builder()
                .message("get Comment Match Success..!!")
                .payload(commentMatchService.searchCommentMatchById(commentId))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Response<CommentMatch>> updateCommentMatchById(
            @PathVariable Integer  commentId,
            @RequestBody CommentMatchRequestUpdate commentMatchRequest
    ) {
        Response<CommentMatch> response = Response.<CommentMatch>builder()
                .message("Update Comment Match Success..!!")
                .payload(commentMatchService.updateCommentMatchById(commentId,commentMatchRequest,getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/match/{matchId}")
    @Operation(summary = "search-comment-by-match-id")
    public ResponseEntity<Response<List<CommentMatch>>> searchCommentByMatchId(
            @PathVariable Integer matchId
    ) {
        Response<List<CommentMatch>> response = Response.<List<CommentMatch>>builder()
                .message("get  Comment by match id Success..!!")
                .payload(commentMatchService.searchCommentByMatchId(matchId))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<Response<CommentMatch>> deleteCommentMatchById(
            @RequestParam Integer  commentId
    ) {
        Response<CommentMatch> response = Response.<CommentMatch>builder()
                .message("Delete Comment Match Success..!!")
                .payload(commentMatchService.deleteCommentMatchById(commentId,getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/discuss")
    public ResponseEntity <Response<CommentMatch>> insertCommentDiscuss(
            @RequestBody CommentMatchRequest commentMatchRequest
    ) {
        Response<CommentMatch> response = Response.<CommentMatch>builder()
                .message("Post discuss Comment Success..!!")
                .payload(commentMatchService.insertCommentDiscuss(commentMatchRequest,getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

}
