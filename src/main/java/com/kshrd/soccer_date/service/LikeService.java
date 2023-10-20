package com.kshrd.soccer_date.service;

import com.kshrd.soccer_date.model.Like;
import com.kshrd.soccer_date.model.request.LikeRequest;

public interface LikeService {
    Like addLike(LikeRequest request,Integer user_id);
    void deletePost(Integer id,Integer user_id);
}
