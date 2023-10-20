package com.kshrd.soccer_date.service;

import com.kshrd.soccer_date.model.RequestMatch;
import com.kshrd.soccer_date.model.request.RequestMatchRequest;

import java.util.List;

public interface RequestMatchService {
    RequestMatch addNewRequest(RequestMatchRequest request,Integer user_id);
    List<RequestMatch>getAllRequestMatchByUserId(Integer id,Integer home_id);
    RequestMatch updateMatchRequest(Integer status,Integer match_id,Integer id);
}
