package com.kshrd.soccer_date.controller;

import com.kshrd.soccer_date.model.RequestMatch;
import com.kshrd.soccer_date.model.Team;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.request.RequestMatchRequest;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.RequestMatchService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/match")
@SecurityRequirement(name = "auth")
public class RequestMatchController {
    private final RequestMatchService service;
    @Value("${image.url}")
    private String baseUrl;
    private static Integer currentId() {
        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUserId();
    }

    public RequestMatchController(RequestMatchService service) {
        this.service = service;
    }

    @PostMapping("/request")
    public ResponseEntity<Response<RequestMatch>> addNewRequestMatch(@RequestBody RequestMatchRequest request) {
        Response<RequestMatch> response = Response.<RequestMatch>builder()
                .message("Request has been sent successfully..!!")
                .payload(service.addNewRequest(request, currentId()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/request/{home_id}")
    public ResponseEntity<Response<List<RequestMatch>>> getTeamRequest(@PathVariable("home_id")Integer home_id) {
       List<RequestMatch> requestMatches=service.getAllRequestMatchByUserId(currentId(),home_id);
        for (RequestMatch rq:requestMatches
             ) {
            rq.getHomeTeam().setLogo(baseUrl+rq.getHomeTeam().getLogo());
            rq.getTeam().setLogo(baseUrl+rq.getTeam().getLogo());
        }
        Response<List<RequestMatch>> response = Response.<List<RequestMatch>>builder()
                .message("Get all team request successfully..!!")
                .payload(requestMatches)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/request/{id}")
    public ResponseEntity<Response<RequestMatch>> UpdateStatusRequestMatch(@PathVariable("id") Integer id, @RequestParam Integer match_id, @RequestParam Integer status) {
        Response<RequestMatch> response = Response.<RequestMatch>builder()
                .message("Updated team request successfully..!!")
                .payload(service.updateMatchRequest(status, match_id, id))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
