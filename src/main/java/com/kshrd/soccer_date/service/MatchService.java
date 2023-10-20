package com.kshrd.soccer_date.service;

import com.kshrd.soccer_date.model.Match;
import com.kshrd.soccer_date.model.request.MatchRequest;
import com.kshrd.soccer_date.model.request.MatchUpdateScoreRequest;

import java.util.List;

public interface MatchService {

    Match createMatch(MatchRequest matchRequest, String email,Integer teamId);
    List<Match> getAllMatch(Integer pageSize,String email) ;
    Match deleteMatchById(Integer matchId,String email);
    Match updateMatchById(MatchRequest matchRequest, String email,Integer matchId);
    Match searchMatchById(Integer matchId);
    Match updateScoreMatch(MatchUpdateScoreRequest matchUpdateScoreRequest,String email,Integer matchId);
    List<Match> searchMatchByGameType(String gametype,Integer teamId);
    List<Match> searchMatchByTeamId(Integer teamId);
    List<Match>getApprovedTeamByTeamId(Integer team_id);
    List<Match> searchMatchByLocationName(String locationName);
    List<Match>upcomingMatch(Integer user_id,Integer limit);
}
