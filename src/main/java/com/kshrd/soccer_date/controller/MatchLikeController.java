package com.kshrd.soccer_date.controller;
import com.kshrd.soccer_date.model.Like;
import com.kshrd.soccer_date.model.Match;
import com.kshrd.soccer_date.model.MatchLike;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.request.LikeRequest;
import com.kshrd.soccer_date.model.request.MatchLikeRequest;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.LikeService;
import com.kshrd.soccer_date.service.MatchLikeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/match")
@SecurityRequirement(name = "auth")
public class MatchLikeController {
    private final MatchLikeService service;

    private static Integer currentId() {
        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUserId();
    }
    public MatchLikeController(MatchLikeService service) {
        this.service = service;
    }
    @PostMapping("/like")
    public ResponseEntity<Response<MatchLike>> like_post(@RequestBody MatchLikeRequest likeRequest) {
        Response<MatchLike> response = Response.<MatchLike>builder()
                .message("Match has been liked  success..!!")
                .payload(service.addLikeMatch(likeRequest, currentId()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/like/{id}")
    public ResponseEntity<Response<Like>> like_delete(@PathVariable("id") Integer id) {
        service.deleteLikeMatch(id, currentId());
        Response<Like> response = Response.<Like>builder()
                .message("Delete is successfully!")
                .payload(null)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
