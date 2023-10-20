package com.kshrd.soccer_date.controller;

import com.kshrd.soccer_date.model.Chat;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.request.ChatRequest;
import com.kshrd.soccer_date.model.response.EditChatRequest;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.ChatService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@SecurityRequirement(name = "auth")
public class ChatController {
    private final ChatService service;
    public ChatController(ChatService service) {
        this.service = service;
    }
    private static Integer currentId() {
        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUserId();
    }
    @PostMapping("/chat")
    public ResponseEntity<Response<Chat>> addNewChat(@RequestBody ChatRequest request) {
        Response<Chat> response = Response.<Chat>builder()
                .message("Chat has been successfully..!!")
                .payload(service.addNewChat(request, currentId()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/chat/{teamId}")
    public ResponseEntity<Response<List<Chat>>> getAllChat(@PathVariable("teamId") Integer team_id) {
        Response<List<Chat>> response = Response.<List<Chat>>builder()
                .message("Get all chat by team_id successfully..!!")
                .payload(service.getAllChat(team_id,currentId()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @PutMapping("/chat/{id}")
    public ResponseEntity<Response<Chat>> editChat(@PathVariable("id") Integer id, @RequestBody EditChatRequest request) {
        Response<Chat> response = Response.<Chat>builder()
                .message("Get all chat by team_id successfully..!!")
                .payload(service.editChat(request,id,currentId()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/chat/{id}")
    public ResponseEntity<Response<Chat>> deleteChat(@PathVariable("id") Integer id) {
        Response<Chat> response = Response.<Chat>builder()
                .message("Message has been deleted successfully..!!")
                .payload(service.deleteChat(id,currentId()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
