package com.kshrd.soccer_date.service;

import com.kshrd.soccer_date.model.Comment;
import com.kshrd.soccer_date.model.Like;
import com.kshrd.soccer_date.model.request.CommentRequest;
import com.kshrd.soccer_date.model.request.UpdateCommentRequest;

import java.util.List;


public interface CommentService {
    Comment commentOnPost(CommentRequest comment,Integer user_id);
    Comment editComment(UpdateCommentRequest commentRequest,Integer user_id);
    void removeComment(Integer id,Integer user_id);
    List<Comment>getCommentByPostId(Integer post_id);
}
