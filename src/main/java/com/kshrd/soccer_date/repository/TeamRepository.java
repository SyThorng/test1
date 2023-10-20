package com.kshrd.soccer_date.repository;

import com.kshrd.soccer_date.model.Team;
import com.kshrd.soccer_date.model.dto.UserAppDTO;
import com.kshrd.soccer_date.model.request.TeamRequestUpdate;
import com.kshrd.soccer_date.model.response.TeamMemberResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeamRepository {

    @Select("""
            INSERT INTO team (name,logo)
            VALUES (#{nameTeam},
                    #{fileName}
                    )
            returning id
            """)
    Integer createTeam(String nameTeam, String fileName);

    @Select("""
            INSERT INTO team_member (team_id, player_id,player_number,role_id, is_owner)
            VALUES (
                    #{teamId},
                    #{userId},
                    #{number},
                    #{roleId},
                    true
                    );
            """)
    void addTeamIdToTeamMember(Integer userId, Integer teamId, Integer roleId, Integer number);

    @Select("""
            SELECT skill from app_user where email=#{email}
            """)
    String getSkillUserByUserId(String email);



    @Select("""
            SELECT name FROM team where name=#{name}
            """)
    String findTeamName(String name);

    @Select("""
            SELECT * FROM  team join team_member tm on team.id = tm.team_id
            join app_user au on tm.player_id = au.id
            join role_play rp on tm.role_id = rp.id
            where team_id=#{teamId}
            """)
    @Results(id = "teamMap", value = {
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "playerNumber", column = "player_number"),
            @Result(property = "rolePlay", column = "role_name"),
            @Result(property = "profile", column = "profile_image")
    })
    List<TeamMemberResponse> getMemberByTeamId(Integer teamId);


    @Select("""
            SELECT *
            FROM app_user
                     join team_member tm on app_user.id = tm.player_id
            where team_id = #{teamId}
              and is_owner = true
            """)
    @Result(property = "userId",column = "id")
    @Result(property = "firstName",column = "first_name")
    @Result(property = "lastName",column = "last_name")
    @Result(property = "profile",column = "profile_image")
    UserAppDTO searchOwnerTeamByTeamId(Integer teamId);




    @Select("""
            SELECT * FROM team join team_member tm on team.id = tm.team_id
            join role_play rp on rp.id = tm.role_id
             
            """)
    @Results(id = "team", value = {
            @Result(property = "teamId", column = "id"),
            @Result(property = "members", column = "team_id",
                    many = @Many(select = "getMemberByTeamId")),
            @Result(property = "userOwner",column = "team_id",
                    one = @One(select = "searchOwnerTeamByTeamId")
            )
    })
    List<Team> getAllTeamByUserId(Integer userId);

    @Select("""
             SELECT is_owner FROM team join team_member tm on team.id = tm.team_id
             where player_id=#{userId} and team_id=#{teamId}
            """)
    Boolean getIsOwnerByUserId(Integer userId, Integer teamId);

    @Select("""
            DELETE FROM team where id=#{teamId} returning *
            """)
    @ResultMap("team")
    Team deleteTeamByTeamId(Integer teamId);


    @Select("""
            UPDATE team set name=#{team.name},
            logo=#{team.imgUrl},
            description=#{team.description}
            where id=#{teamId}
            returning *
            """)
    @ResultMap("team")
    Team updateTeamByOwner(Integer teamId, @Param("team") TeamRequestUpdate teamRequestUpdate, Integer userId);

    @Select("""
            DELETE from team_member where player_id=#{memberId} and team_id=#{teamId}
            """)
    @ResultMap("teamMap")
    Integer deleteMemberById(Integer memberId, Integer teamId);

    @Select("""
             SELECT is_owner FROM team_member where player_id=#{memberId} and team_id=#{teamId}
            """)
    boolean getIsOwnerByMemberId(Integer memberId, Integer teamId);

    @Select("""
            SELECT * FROM  team join team_member tm on team.id = tm.team_id
                                                           join app_user au on tm.player_id = au.id
                                                           join role_play rp on tm.role_id = rp.id
                                         where au.id=#{memberId} 
            """)
    @ResultMap("teamMap")
    TeamMemberResponse searchMemberById(Integer memberId);

    @Select("""
            DELETE from team_member where player_id=#{userId} and team_id=#{teamId} returning  team_id
            """)
    Integer leaveTeamByUser(Integer userId, Integer teamId);


    @Select("""
            SELECT * FROM team join team_member tm on team.id = tm.team_id where player_id=#{userId}
                        
            """)
    @ResultMap("team")
    List<Team> getAllTeamByCurrentUser(Integer userId);

    @Select("""
            SELECT * FROM team order by id offset #{pageNo} limit #{pageSize}
            """)
    @ResultMap("team")
    List<Team> getAllTeam(Integer pageSize,Integer pageNo);

    @Select("""
            SELECT id from team_member where player_id =#{userId} and team_id=#{teamId}
            """)
    Integer getTeamMemberIdByUserIdTeamId(Integer teamId, Integer userId);

    @Select("""
            SELECT * FROM team where name ilike (concat('%',#{teamName},'%'))  limit #{pageSize} offset #{pageNo}
            """)
    @ResultMap("team")
    List<Team> searchTeamByName(String teamName,Integer pageSize,Integer pageNo);

    @Select("""
            SELECT * FROM team_member where team_id =#{teamId} and player_id=#{userId}
            """)
    Integer findMemberByTeamId(Integer teamId, Integer userId);


    @Select("""
            UPDATE team_member
            SET role_id=#{roleId}
            where team_id = #{teamId} and player_id = #{playerId}
            returning id
            """)
    Integer updateRolePlayerByTeamOwner(Integer teamId, Integer playerId, Integer roleId);

    @Select("""
            SELECT logo from team where id=#{teamId}
            """)
    String searchLogoByTeamId(Integer teamId);


    @Select("""
            SELECT tm.player_id FROM team_member tm where team_id=#{teamId} and is_owner=true
            """)
    Integer getTeamOwnerIdByTeamId(Integer teamId);

    @Select("""
            SELECT * FROM  team join team_member tm on team.id = tm.team_id
                                join app_user au on tm.player_id = au.id
                                join role_play rp on tm.role_id = rp.id
            where au.id=#{memberId} and team_id=#{teamId}
            """)
    @ResultMap("teamMap")
    TeamMemberResponse searchMemberByTeamId(Integer memberId,Integer teamId);

    @Select("""
            SELECT *
            from team_member
                     join app_user au on team_member.player_id = au.id
            where team_id = #{teamId} and au.skill ilike  (concat('%',#{skill},'%'))
            """)
    @ResultMap("teamMap")
    List<TeamMemberResponse> filterSkillMemberInTeam(Integer teamId, String skill);



    @Select("""
            SELECT *
            from team where id=#{teamId}
            """)
    @ResultMap("team")

    Team searchTeamById(Integer teamId);


    @Select("""
            SELECT description from team where id=#{teamId}
            """)
    String getDescriptionByTeamId(Integer teamId);
}
