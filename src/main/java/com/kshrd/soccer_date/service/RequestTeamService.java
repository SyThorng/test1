package com.kshrd.soccer_date.service;

import com.kshrd.soccer_date.model.RequestTeam;
import com.kshrd.soccer_date.model.request.RequestTeamRequest;

import java.util.List;

public interface RequestTeamService {
    RequestTeam addNewRequest(RequestTeamRequest request,Integer user_id);
    RequestTeam updateStatus(Integer id,Integer userId,Integer status);
    List<RequestTeam>getAllPlayerRequest(Integer userId,Integer team_id);
}
