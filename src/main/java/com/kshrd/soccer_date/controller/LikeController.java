package com.kshrd.soccer_date.controller;
import com.kshrd.soccer_date.model.Like;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.request.LikeRequest;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.LikeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@SecurityRequirement(name = "auth")
public class LikeController {
    private final LikeService service;

    private static Integer currentId() {
        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUserId();
    }

    public LikeController(LikeService service) {
        this.service = service;
    }

    @PostMapping("/like")
    public ResponseEntity<Response<Like>> like_post(@RequestBody LikeRequest likeRequest) {
        Response<Like> response = Response.<Like>builder()
                .message("Post has been liked  success..!!")
                .payload(service.addLike(likeRequest, currentId()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/like/{id}")
    public ResponseEntity<Response<Like>> like_delete(@PathVariable("id") Integer id) {
        service.deletePost(id, currentId());
        Response<Like> response = Response.<Like>builder()
                .message("Delete is successfully!")
                .payload(null)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
