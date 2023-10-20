package com.kshrd.soccer_date.service;

import com.kshrd.soccer_date.model.Chat;
import com.kshrd.soccer_date.model.request.ChatRequest;
import com.kshrd.soccer_date.model.response.EditChatRequest;

import java.util.List;

public interface ChatService {
    Chat addNewChat(ChatRequest request,Integer user_id);
    List<Chat> getAllChat(Integer team_id,Integer user_id);

    Chat editChat(EditChatRequest request, Integer id, Integer user_id);

    Chat deleteChat(Integer id,Integer user_id);
}
