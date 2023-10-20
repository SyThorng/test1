package com.kshrd.soccer_date.service;

import com.kshrd.soccer_date.model.InvitePlayer;
import com.kshrd.soccer_date.model.request.InvitePlayerRequest;

import java.util.List;

public interface InvitePlayerService {
    InvitePlayer invitePlayer(InvitePlayerRequest request, Integer userId);
    List<InvitePlayer> getInviterByCurrentId(Integer id);
    InvitePlayer acceptTeamRequest(Integer status,Integer id);

}
