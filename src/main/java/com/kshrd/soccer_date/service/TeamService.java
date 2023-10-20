package com.kshrd.soccer_date.service;

import com.kshrd.soccer_date.model.Team;
import com.kshrd.soccer_date.model.dto.UserAppDTO;
import com.kshrd.soccer_date.model.request.TeamRequestUpdate;
import com.kshrd.soccer_date.model.response.TeamMemberResponse;

import java.util.List;

public interface TeamService {
    Team createTeam(String nameTeam, String fileName,String email);
    List<Team> getAllTeam(String email,Integer pageSize,Integer pageNo);
    Team deleteTeamByUser(Integer teamId,String email);
    Team updateTeamByOwner(Integer teamId, TeamRequestUpdate teamRequestUpdate,String email);
    UserAppDTO kickMemberByMemberId(Integer memberId, String email, Integer teamId);
    Team leaveTeamByUser(Integer teamId,String email);
    Team searchTeamById(Integer teamId,String email);
    List<Team> getAllTeamByCurrentUser(String email);
    List<Team> searchTeamByName(String teamName,Integer pageSize,Integer pageNo);
    TeamMemberResponse updateRolePlayerByTeamOwner(Integer teamId,Integer playerId,String email,String roleName);
    List<TeamMemberResponse> filterSkillMemberInTeam(Integer teamId,String skill);
}
