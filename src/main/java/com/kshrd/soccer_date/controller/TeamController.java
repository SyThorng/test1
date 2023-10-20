package com.kshrd.soccer_date.controller;

import com.kshrd.soccer_date.model.Team;
import com.kshrd.soccer_date.model.dto.UserAppDTO;
import com.kshrd.soccer_date.model.request.TeamRequest;
import com.kshrd.soccer_date.model.request.TeamRequestUpdate;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.FileUploadFileService;
import com.kshrd.soccer_date.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/v1/team")
@RestController
@SecurityRequirement(name = "auth")
public class TeamController {

    private final TeamService teamService;
    private final FileUploadFileService fileUploadFileService;

    public TeamController(TeamService teamService, FileUploadFileService fileUploadFileService) {
        this.teamService = teamService;
        this.fileUploadFileService = fileUploadFileService;
    }

    String getUsernameOfCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        return username;
    }


    @PostMapping(value = "")
    @Operation(summary = "create-team")
    public ResponseEntity<Response<Team>>  createTeam(
            @RequestBody TeamRequest teamRequest
    ) {
        Response<Team> response = Response.<Team>builder()
                .message("Create Team Successfully..!!")
                .payload(teamService.createTeam(teamRequest.getName(),teamRequest.getImgUrl(),getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/teams")
    @Operation(summary = "get-all-team")
    public ResponseEntity<Response<List<Team>>> getAllTeam(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize
    ){
        Response<List<Team>> response = Response.  <List<Team>>builder()
                .message("Get All Team success...!!")
                .team(teamService.getAllTeam(getUsernameOfCurrentUser(),pageNo,pageSize))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Response<Team>>  deleteTeamByUser
            (
                    @PathVariable Integer teamId
            ){
        Response<Team> response = Response.<Team>builder()
                .message("Delete Team Success...!!")
                .team(teamService.deleteTeamByUser(teamId,getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{teamId}")
    @Operation(summary = "update-team-by-id")
    public ResponseEntity<Response<Team>>  updateTeamByOwner
            (
                    @PathVariable Integer teamId,
                    @RequestBody TeamRequestUpdate teamRequestUpdate
            ){
        Response<Team> response = Response.<Team>builder()
                .message("Update Team Success...!!")
                .team(teamService.updateTeamByOwner(teamId,teamRequestUpdate,getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{teamId}/{memberId}/kick")
    public ResponseEntity<Response<UserAppDTO>>  deleteMemberById
            (
                    @PathVariable Integer teamId,
                    @PathVariable Integer memberId
            ){
        Response<UserAppDTO> response = Response.<UserAppDTO>builder()
                .message("kick member is Success...!!")
                .payload(teamService.kickMemberByMemberId(memberId,getUsernameOfCurrentUser(),teamId))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{teamId}/leave-team")
    public ResponseEntity<Response<Team>>  leaveTeamByUser
            (
                    @PathVariable Integer teamId
            ){
        Response<Team> response = Response.<Team>builder()
                .message("Leave Team Success...!!")
                .payload(teamService.leaveTeamByUser(teamId,getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/{teamId}")
    public ResponseEntity<Response<Team>>  searchTeamById
            (
                    @PathVariable Integer teamId
            ){
        Response<Team> response = Response.<Team>builder()
                .message("Get Team Success...!!")
                .payload(teamService.searchTeamById(teamId,getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("")
    @Operation(summary = "get-all-team-by-current-user")
    public ResponseEntity<Response<List<Team>>> getAllTeamByCurrentUser(){
        Response<List<Team>> response = Response.<List<Team>>builder()
                .message("Get All Team success...!!")
                .team(teamService.getAllTeamByCurrentUser(getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/search-team-by-name/{teamName}")
    public ResponseEntity<Response<List<Team>>> searchTeamByName(
            @RequestParam String teamName,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize
    ){
        Response<List<Team>> response = Response.<List<Team>>builder()
                .message("Get Team Name success...!!")
                .team(teamService.searchTeamByName(teamName,pageSize,pageNo))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }


    @PutMapping("/{teamId}/{playId}/update-role")
    public ResponseEntity<?>  updateRolePlayerByTeamOwner
            (
                    @PathVariable Integer teamId,
                    @PathVariable Integer playId,
                    @RequestParam String  roleName
            ){
        Response response = Response.builder()
                .message("Update Role Player is Success...!!")
                .payload(teamService.updateRolePlayerByTeamOwner(teamId,playId,getUsernameOfCurrentUser(),roleName))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/{teamId}/skill")
    public ResponseEntity<?>  filterSkillMemberInTeam
            (
                    @PathVariable Integer teamId,
                    @RequestParam String  skill
            ){
        Response response = Response.builder()
                .message("get skill Player in team  Success...!!")
                .payload(teamService.filterSkillMemberInTeam(teamId,skill))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
}