package com.kshrd.soccer_date.controller;

import com.kshrd.soccer_date.model.InvitePlayer;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.request.InvitePlayerRequest;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.InvitePlayerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@SecurityRequirement(name = "auth")
public class InvitePlayerController {
    private final InvitePlayerService service;

    private static Integer currentId() {
        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUserId();
    }
    @Value("${image.url}")
    private String baseUrl;
    public InvitePlayerController(InvitePlayerService service) {
        this.service = service;
    }

    @PostMapping("/invite")
    public ResponseEntity<Response<InvitePlayer>> invitePlayer(@RequestBody InvitePlayerRequest request) {
        Response<InvitePlayer> response = Response.<InvitePlayer>builder()
                .message("Your Team has been invited player successfully!")
                .payload(service.invitePlayer(request, currentId()))
                .status(200).build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/invite")
    public ResponseEntity<Response<List<InvitePlayer>>> getInviterByCurrntId() {
        List<InvitePlayer> invitePlayers=service.getInviterByCurrentId(currentId());
        for (InvitePlayer invitePlayer:invitePlayers){
            invitePlayer.getTeam().setLogo(baseUrl+invitePlayer.getTeam().getLogo());
        }
        Response<List<InvitePlayer>> response = Response.<List<InvitePlayer>>builder()
                .message("Get All teams inviter successfully!")
                .payload(invitePlayers)
                .status(200).build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/invite/{id}")
    public ResponseEntity<Response<InvitePlayer>> acceptPlayer(@PathVariable Integer id, @RequestParam Integer status) {
        Response<InvitePlayer> response = Response.<InvitePlayer>builder()
                .message("Get All teams inviter successfully!")
                .payload(service.acceptTeamRequest(status, id))
                .status(200).build();
        return ResponseEntity.ok().body(response);
    }
}
