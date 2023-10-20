package com.kshrd.soccer_date.service;

import com.kshrd.soccer_date.model.PlayerScore;
import com.kshrd.soccer_date.model.request.PlayerScoreRequest;

import java.util.List;

public interface PlayerScoreService {
    PlayerScore addScore(PlayerScoreRequest request);
    List<PlayerScore> getScore(Integer match_id,Integer team_id);

}
