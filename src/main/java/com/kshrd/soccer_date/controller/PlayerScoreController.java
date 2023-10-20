package com.kshrd.soccer_date.controller;

import com.kshrd.soccer_date.model.PlayerScore;
import com.kshrd.soccer_date.model.request.PlayerScoreRequest;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.PlayerScoreService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@SecurityRequirement(name = "auth")
public class PlayerScoreController {
    private final PlayerScoreService service;

    public PlayerScoreController(PlayerScoreService service) {
        this.service = service;
    }

    @PostMapping("/score")
    public ResponseEntity<Response<PlayerScore>> addScore(@RequestBody PlayerScoreRequest request) {
        Response<PlayerScore> response = Response.<PlayerScore>
                        builder()
                .message("Add new score successfully..!")
                .payload(service.addScore(request))
                .status(200).build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/score/{match_id}/{team_id}")
    public ResponseEntity<Response<List<PlayerScore>>> getScore(@PathVariable("match_id") Integer match_id, @PathVariable("team_id")
    Integer team_id
    ) {
        Response<List<PlayerScore>> response = Response.<List<PlayerScore>>
                        builder()
                .message("Get score in match score successfully..!")
                .payload(service.getScore(match_id, team_id))
                .status(200).build();
        return ResponseEntity.ok().body(response);
    }
}
