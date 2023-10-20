package com.kshrd.soccer_date.controller;
import com.kshrd.soccer_date.model.RequestTeam;
import com.kshrd.soccer_date.model.Team;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.request.RequestTeamRequest;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.RequestTeamService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@SecurityRequirement(name = "auth")
public class RequestTeamController {

    public RequestTeamController(RequestTeamService service) {
        this.service = service;
    }
    @Value("${image.url}")
    private String baseUrl;
    private static Integer currentId() {
        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUserId();
    }
    private  final RequestTeamService service;
    @PostMapping("/request")
    public ResponseEntity<Response<RequestTeam>> addNewRequest(@RequestBody RequestTeamRequest request) {
        RequestTeam requestTeam=service.addNewRequest(request, currentId());
        requestTeam.getPlayer().setProfile(baseUrl+requestTeam.getPlayer().getProfile());
        Response<RequestTeam> response = Response.<RequestTeam>builder()
                .message("Request has been sent successfully..!!")
                .payload(requestTeam)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @PutMapping("/request/{id}")
    public ResponseEntity<Response<RequestTeam>> updateStatus(@PathVariable Integer id,@RequestParam Integer status) {
        RequestTeam requestTeam=service.updateStatus(id,currentId(),status);
        requestTeam.getPlayer().setProfile(baseUrl+requestTeam.getPlayer().getProfile());
        Response<RequestTeam> response = Response.<RequestTeam>builder()
                .message("Request has been updated successfully..!!")
                .payload(requestTeam)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/request/{team_id}")
    public ResponseEntity<Response<List<RequestTeam>>> getAllPlayerRequest(@PathVariable("team_id") Integer team_id) {
        List<RequestTeam>teams=service.getAllPlayerRequest(currentId(),team_id);
        Response<List<RequestTeam>> response = Response.<List<RequestTeam>>builder()
                .message("Get all player request to join your team successfully..!!")
                .payload(teams)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
