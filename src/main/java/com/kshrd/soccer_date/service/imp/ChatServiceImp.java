package com.kshrd.soccer_date.service.imp;
import com.kshrd.soccer_date.exception.NotOwnerException;
import com.kshrd.soccer_date.model.Chat;
import com.kshrd.soccer_date.model.request.ChatRequest;
import com.kshrd.soccer_date.model.response.EditChatRequest;
import com.kshrd.soccer_date.repository.ChatRepository;
import com.kshrd.soccer_date.service.ChatService;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImp implements ChatService {
    private final ChatRepository repository;

    public ChatServiceImp(ChatRepository repository) {
        this.repository = repository;
    }

    @Override
    public Chat addNewChat(ChatRequest request, Integer user_id) {
        if(repository.member(user_id,request.getTeam_id())==null){
            throw new NotOwnerException("Can not chat","You are not member this team!");
        }
        return repository.addNewChat(request,user_id);
    }

    @Override
    public List<Chat> getAllChat(Integer team_id,Integer user_id) {
        return repository.getAllChats(team_id,user_id);
    }

    @Override
    public Chat editChat(EditChatRequest request, Integer id, Integer user_id) {
        Chat chat=repository.editChat(request,id,user_id);
        if (chat==null){
            throw new NotOwnerException("Not Owner!","You can edit this message");
        }
        return chat;
    }

    @Override
    public Chat deleteChat(Integer id, Integer user_id) {
        Integer deleted=repository.deleteChat(id,user_id);
        if (deleted!=1){
            throw new NotOwnerException("Not Owner!","You can delete this message");
        }
        return null;
    }
}
