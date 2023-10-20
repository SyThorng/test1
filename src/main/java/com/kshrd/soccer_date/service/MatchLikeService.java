package com.kshrd.soccer_date.service;

import com.kshrd.soccer_date.model.MatchLike;
import com.kshrd.soccer_date.model.request.MatchLikeRequest;

public interface MatchLikeService {
    MatchLike addLikeMatch(MatchLikeRequest request, Integer user_id);
    void deleteLikeMatch(Integer id,Integer user_id);
    Integer searchIdLikeMatch(Integer match_id);
}
