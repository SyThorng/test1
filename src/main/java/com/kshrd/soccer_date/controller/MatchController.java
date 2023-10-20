package com.kshrd.soccer_date.controller;

import com.kshrd.soccer_date.model.Match;
import com.kshrd.soccer_date.model.Team;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.request.MatchRequest;
import com.kshrd.soccer_date.model.request.MatchUpdateScoreRequest;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.MatchService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@SecurityRequirement(name = "auth")
public class MatchController {
    @Value("${image.url}")
    private String baseUrl;
    private final MatchService matchService;
    private static Integer currentId() {
        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUserId();
    }

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    String getUsernameOfCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        return username;
    }

    @PostMapping("/match")
    public ResponseEntity<Response<Match>> createMatch(
            @RequestBody MatchRequest matchRequest,
            @RequestParam Integer teamId
    ) {

        Response<Match> response = Response.<Match>builder()
                .message("Create Match Success...!!")
                .payload(matchService.createMatch(matchRequest, getUsernameOfCurrentUser(), teamId))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/match")
    public ResponseEntity<Response<List<Match>>> getAllMatch(@RequestParam Integer pageSize) {
        Response<List<Match>> response = Response.<List<Match>>builder()
                .message("get all Match Success..!!")
                .payload(matchService.getAllMatch(pageSize,getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/match/{matchId}")
    public ResponseEntity<Response<Match>> deleteMatchById(
            @PathVariable Integer matchId
    ) {
        Response<Match> response = Response.<Match>builder()
                .message("Delete Match Post Success..!!")
                .payload(matchService.deleteMatchById(matchId, getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/match/{matchId}")
    public ResponseEntity<Response<Match>> updateMatchById(
            @RequestBody MatchRequest matchRequest,
            @PathVariable("matchId") Integer matchId
    ) {
        Response<Match> response=Response.<Match>builder()
                .message("Update Match Post Success..!!")
                .payload(matchService.updateMatchById(matchRequest, getUsernameOfCurrentUser(), matchId))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/match/{matchId}")
    public ResponseEntity<Response<Match>> searchMatchById(
            @PathVariable Integer matchId
    ) {
        Response<Match> response=Response.<Match>builder()
                .message("Get Match Success..!!")
                .payload(matchService.searchMatchById(matchId))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/match/approved/{team_id}")
    public ResponseEntity<Response<List<Match>>> getApprovedTeamByTeamId(
            @PathVariable("team_id") Integer team_id
    ) {
        List<Match>matches=matchService.getApprovedTeamByTeamId(team_id);

        Response<List<Match>> response=Response.<List<Match>>builder()
                .message("Get match which has been approved Success..!!")
                .payload(matches)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }


    @PutMapping("/match/{matchId}/score")
    public ResponseEntity<Response<Match>> updateScoreMatch(
            @RequestBody MatchUpdateScoreRequest matchUpdateScoreRequest,
            @PathVariable("matchId") Integer matchId
    ) {
        Response<Match> response = Response.<Match>builder()
                .message("update score Match Success..!!")
                .payload(matchService.updateScoreMatch(matchUpdateScoreRequest, getUsernameOfCurrentUser(), matchId))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/match/gameType/{teamId}")
    public ResponseEntity<Response<List<Match>>> searchMatchByGameType(
            @RequestParam String gametype,
            @PathVariable Integer teamId
    ) {
        Response<List<Match>> response = Response.<List<Match>>builder()
                .message("get all Match upcoming Success..!!")
                .payload(matchService.searchMatchByGameType(gametype, teamId))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/match/{teamId}/team")
    public ResponseEntity<?> searchMatchByTeamId(
            @PathVariable Integer teamId
    ) {
        Response<List<Match>> response = Response.<List<Match>>builder()
                .message("get all Match by Team id Success..!!")
                .payload(matchService.searchMatchByTeamId(teamId))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/match/locationName")
    public ResponseEntity<?> searchMatchByLocationName(
           @RequestParam String locationName
    ) {
        Response<List<Match>> response = Response.<List<Match>>builder()
                .message("get all Match by location name  Success..!!")
                .payload(matchService.searchMatchByLocationName(locationName))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/match/upcoming/")
    public ResponseEntity<?> getUpcomingMatch(
           @RequestParam("limit") Integer limit
    ) {
        Response<List<Match>> response = Response.<List<Match>>builder()
                .message("get all Match by location name  Success..!!")
                .payload(matchService.upcomingMatch(currentId(),limit))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }


}
