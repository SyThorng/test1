package com.kshrd.soccer_date.service.imp;

import com.kshrd.soccer_date.enums.ERolePlayer;
import com.kshrd.soccer_date.exception.FieldNotFoundException;
import com.kshrd.soccer_date.exception.InvalidFieldException;
import com.kshrd.soccer_date.mapper.UserMapper;
import com.kshrd.soccer_date.model.Team;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.dto.UserAppDTO;
import com.kshrd.soccer_date.model.request.TeamRequestUpdate;
import com.kshrd.soccer_date.model.response.TeamMemberResponse;
import com.kshrd.soccer_date.repository.TeamRepository;
import com.kshrd.soccer_date.repository.UserRepository;
import com.kshrd.soccer_date.service.FileUploadFileService;
import com.kshrd.soccer_date.service.TeamService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class TeamServiceImp implements TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamServiceImp(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @Value("${image.url}")
    private String imageUrl;

    @Override
    public Team createTeam(String nameTeam, String fileName, String email) {
        if (teamRepository.findTeamName(nameTeam) != null) {
            throw new InvalidFieldException(
                    "Invalid Team",
                    "Team already create , please Input other name to Create Team"
            );
        }

        if (nameTeam.isEmpty()) {
            throw new InvalidFieldException(
                    "Invalid field",
                    "Name Team can't empty"
            );
        }

        if (fileName.isEmpty()) {
            fileName = "DefaultLogoTeam.png";
        }

        Integer number = userRepository.getNumberByUserId(userRepository.getUserIdByEmail(email));

        Integer teamId = teamRepository.createTeam(nameTeam, fileName);

        teamRepository.addTeamIdToTeamMember(userRepository.getUserIdByEmail(email), teamId, 1, number);
        Team team = teamRepository.searchTeamById(teamId);
        team.setLogo(imageUrl + team.getLogo());
        return team;
    }

    @Override
    public List<Team> getAllTeam(String email, Integer pageNo, Integer pageSize) {
//        Integer userid = userRepository.getUserIdByEmail(email);
        List<Team> teams = teamRepository.getAllTeam(pageSize, pageNo);

        teams.forEach(e -> e.setLogo(imageUrl + e.getLogo()));

        if (teams.size() == 0) {
            throw new FieldNotFoundException(
                    "Team invalid",
                    "You Don't have team"
            );
        }
        return teams;
    }

    @Override
    public Team deleteTeamByUser(Integer teamId, String email) {
        Integer userId = userRepository.getUserIdByEmail(email);

        if (teamRepository.searchTeamById(teamId) == null) {
            throw new FieldNotFoundException(
                    "Team Invalid",
                    "Team Id [ " + teamId + " ] is not found...!!"
            );
        }

        Boolean is_owner = teamRepository.getIsOwnerByUserId(userId, teamId);
        if (is_owner == null) {
            throw new InvalidFieldException(
                    "Invalid",
                    "You Not Leader So Cannot Delete this Team"
            );
        }


        if (is_owner == false) {
            throw new InvalidFieldException(
                    "Invalid",
                    "You Not Leader So Cannot Delete this Team"
            );
        }

        Team team = teamRepository.deleteTeamByTeamId(teamId);
        team.setLogo(imageUrl + team.getLogo());
        return team;
    }

    @Override
    public Team updateTeamByOwner(Integer teamId, TeamRequestUpdate teamRequestUpdate, String email) {
        Integer userId = userRepository.getUserIdByEmail(email);
        if (teamRepository.searchTeamById(teamId) == null) {
            throw new FieldNotFoundException(
                    "Team Invalid",
                    "Team Id [ " + teamId + " ] is not found...!!"
            );
        }

        Boolean is_owner = teamRepository.getIsOwnerByUserId(userId, teamId);

        if (is_owner == false) {
            throw new InvalidFieldException(
                    "Invalid",
                    "You Not Leader So Cannot Update this Team"
            );
        }
        Team team = teamRepository.updateTeamByOwner(teamId, teamRequestUpdate, userId);
        team.setLogo(imageUrl + team.getLogo());
        return team;
    }

    @Override
    public UserAppDTO kickMemberByMemberId(Integer memberId, String email, Integer teamId) {
        Integer userId = userRepository.getUserIdByEmail(email);

        if (teamRepository.searchTeamById(teamId) == null) {
            throw new FieldNotFoundException(
                    "Team Invalid",
                    "Team Id [ " + teamId + " ] is not found...!!"
            );
        }

        if (teamRepository.findMemberByTeamId(teamId, memberId) == null) {
            throw new FieldNotFoundException("User Not Found",
                    " member id [" + memberId + "] is not found in team id [" + teamId + "]");
        }

        Boolean is_owner = teamRepository.getIsOwnerByUserId(userId, teamId);

        if (is_owner == null) {
            throw new FieldNotFoundException("User Invalid",
                    " userId [" + userId + "] is not found...!!");
        }
////
        if (is_owner == false) {
            throw new InvalidFieldException(
                    "Invalid",
                    "You Not Leader So Cannot Kick this User"
            );
        }

        if (teamRepository.getIsOwnerByMemberId(memberId, teamId) == true) {
            throw new FieldNotFoundException("User Invalid",
                    "Cannot Kick Owner..!!");
        }

        teamRepository.deleteMemberById(memberId, teamId);
        UserApp userApp = userRepository.searchUserById(memberId);
        return UserMapper.INSTANCE.toUserAppDTO(userApp);
    }

    @Override
    public Team leaveTeamByUser(Integer teamId, String email) {
        Integer userId = userRepository.getUserIdByEmail(email);

        if (teamRepository.searchTeamById(teamId) == null) {
            throw new FieldNotFoundException(
                    "Team Invalid",
                    "Team Id [ " + teamId + " ] is not found...!!"
            );
        }

        Boolean is_owner = teamRepository.getIsOwnerByUserId(userId, teamId);

        if (is_owner == null) {
            throw new FieldNotFoundException("User Invalid",
                    " userId [" + userId + "] is not found...!!");
        }
//
        if (is_owner == true) {
            throw new InvalidFieldException(
                    "Invalid",
                    "You are a Leader So Cannot leave team"
            );
        }


        Integer team = teamRepository.leaveTeamByUser(userId, teamId);
        Team team1 = teamRepository.searchTeamById(team);
        team1.setLogo(imageUrl + team1.getLogo());
        return team1;
    }

    @Override
    public Team searchTeamById(Integer teamId, String email) {
        Integer userId = userRepository.getUserIdByEmail(email);
        if (teamRepository.searchTeamById(teamId) == null) {
            throw new FieldNotFoundException(
                    "Team Invalid",
                    "Team Id [ " + teamId + " ] is not found...!!"
            );
        }

        Team team = teamRepository.searchTeamById(teamId);
        team.setLogo(imageUrl+team.getLogo());

        UserAppDTO userAppDTO = teamRepository.searchOwnerTeamByTeamId(teamId);
        userAppDTO.setProfile(imageUrl + userAppDTO.getProfile());
        List<TeamMemberResponse> teamMemberResponseList = teamRepository.getMemberByTeamId(teamId);

        teamMemberResponseList.forEach(e -> e.setProfile(imageUrl + e.getProfile()));

        team.setMembers(teamMemberResponseList);
        team.setUserOwner(userAppDTO);
        team.setIs_owner(teamRepository.getIsOwnerByUserId(userId, teamId));


        return team;
    }

    @Override
    public List<Team> getAllTeamByCurrentUser(String email) {
        Integer userId = userRepository.getUserIdByEmail(email);
        List<Team> teams = teamRepository.getAllTeamByCurrentUser(userId);
        teams.forEach(team -> team.setLogo(imageUrl + team.getLogo()));
        teams.forEach(team -> team.getMembers().forEach(e -> e.setProfile(imageUrl + e.getProfile())));
        teams.forEach(team -> team.getUserOwner().setProfile(imageUrl+team.getUserOwner().getProfile()));

        return teams;
    }

    @Override
    public List<Team> searchTeamByName(String teamName, Integer pageSize, Integer pageNo) {
        List<Team> teams = teamRepository.searchTeamByName(teamName, pageSize, pageNo);
        if (teams.size() == 0) {
            throw new FieldNotFoundException(
                    "Team Not Found..!!",
                    "Team Name [" + teamName + "] is not found...!!"
            );
        }
        teams.forEach(team -> team.setLogo(imageUrl + team.getLogo()));
        return teams;
    }


    @Override
    public TeamMemberResponse updateRolePlayerByTeamOwner(Integer teamId, Integer playerId, String email, String roleName) {
        Integer userId = userRepository.getUserIdByEmail(email);
        if (teamRepository.searchTeamById(teamId) == null) {
            throw new FieldNotFoundException(
                    "Team Invalid",
                    "Team Id [ " + teamId + " ] is not found...!!"
            );
        }

        if (teamRepository.findMemberByTeamId(teamId, playerId) == null) {
            throw new FieldNotFoundException("User Not Found",
                    " member id [" + playerId + "] is not found in team id [" + teamId + "]");
        }

        Boolean is_owner = teamRepository.getIsOwnerByUserId(userId, teamId);

        if (is_owner == null) {
            throw new FieldNotFoundException("User Invalid",
                    " userId [" + userId + "] is not found...!!");
        }

        if (!is_owner) {
            throw new InvalidFieldException(
                    "Invalid",
                    "You Not Leader So Cannot update this User"
            );
        }

        if (!roleName.equalsIgnoreCase("captain") && !roleName.equalsIgnoreCase("player")) {
            throw new InvalidFieldException(
                    "Field Invalid",
                    "Role Name Invalid ,please  input [ captain or player ] ..!!"
            );
        }

        Integer roleId = 3;

        if (roleName.equals("captain")) {
            roleId = 2;
        }

        teamRepository.updateRolePlayerByTeamOwner(teamId, playerId, roleId);
        TeamMemberResponse teamMemberResponse = teamRepository.searchMemberByTeamId(playerId, teamId);
        teamMemberResponse.setProfile(imageUrl + teamMemberResponse.getProfile());
        return teamMemberResponse;
    }

    @Override
    public List<TeamMemberResponse> filterSkillMemberInTeam(Integer teamId, String skill) {
        boolean is_role = false;
        List<TeamMemberResponse> teamMemberResponseList = teamRepository.filterSkillMemberInTeam(teamId, skill);
        if (teamMemberResponseList.size() == 0) {
            throw new FieldNotFoundException(
                    "skill invalid",
                    "In this team don't have this position"
            );
        }
        for (ERolePlayer rolePlayer : ERolePlayer.values()) {
            if (skill.equalsIgnoreCase(rolePlayer.name())) {
                is_role = true;
                break;
            }
        }

        if (!is_role) {
            throw new InvalidFieldException(
                    "Invalid Skill",
                    "This skill is not correct : " +
                            "please input one of (GK,RB,CB,LB,CMF,DMF,LWF,RWF,CF)");
        }
        teamMemberResponseList.forEach(e -> e.setProfile(imageUrl + e.getProfile()));
        return teamMemberResponseList;
    }
}
