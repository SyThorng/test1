package com.kshrd.soccer_date.service;

import com.kshrd.soccer_date.model.CommentMatch;
import com.kshrd.soccer_date.model.request.CommentMatchRequest;
import com.kshrd.soccer_date.model.request.CommentMatchRequestUpdate;

import java.util.List;

public interface CommentMatchService {
    CommentMatch insertComment(CommentMatchRequest commentMatchRequest,String email);
    List<CommentMatch> getALlCommentMatch();
    List<CommentMatch> searchCommentByMatchId(Integer matchId);
    CommentMatch searchCommentMatchById(Integer commentId);
    CommentMatch updateCommentMatchById(Integer commentId, CommentMatchRequestUpdate commentMatchRequest, String email);
    CommentMatch deleteCommentMatchById(Integer commentId,String email);

    CommentMatch insertCommentDiscuss(CommentMatchRequest commentMatchRequest,String email);

}
