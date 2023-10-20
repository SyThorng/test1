package com.kshrd.soccer_date.repository;
import com.kshrd.soccer_date.model.RequestTeam;
import com.kshrd.soccer_date.model.request.RequestTeamRequest;
import com.kshrd.soccer_date.model.response.TeamMemberResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface RequestTeamRepository {
    @Select("""
            insert into request_team (team_id, player_id) values (#{req.team_id},#{user_id}) returning *
             """)
    @Results(id = "requestTeam", value = {
            @Result(property = "team_id", column = "team_id"),
            @Result(property = "player_id", column = "player_id"),
            @Result(property = "player", column = "player_id",one = @One(select = "getTeamMemberById")),
            @Result(property = "status", column = "status"),
            @Result(property = "create_at", column = "create_at")
    })
    RequestTeam addNewRequest(@Param("req") RequestTeamRequest request, Integer user_id);

    @Select("""
            select * from app_user where id=#{id}
            """)
    @Result(property = "firstName",column = "first_name")
    @Result(property = "lastName",column = "last_name")
    @Result(property = "profile",column = "profile_image")
    @Result(property = "player_id",column = "id")
    TeamMemberResponse getTeamMemberById(Integer id);

    @Select("""
            update request_team set status=#{status} where  id=#{id} returning *
            """)
    @ResultMap("requestTeam")
    RequestTeam updateStatus(Integer id, String status);

    @Select("""
            select team_id from request_team where id=#{id}
            """)
    Integer teamId(Integer id);

    @Select("""
            select is_owner from team_member where player_id=#{id} and team_id=#{team_id}
                    """)
    Boolean teamOwner(Integer id, Integer team_id);

    @Select("""
            select rt.id, rt.team_id, rt.player_id, rt.status, rt.create_at
            from team_member tm
                     inner join request_team rt on tm.team_id = rt.team_id
            where tm.player_id = #{userId}
              and is_owner = true
              and rt.status = 'pending' and rt.team_id=#{team_id}
            """)
    @ResultMap("requestTeam")
    List<RequestTeam> getPlayerRequest(Integer userId,Integer team_id);
    @Select("""
            select status from request_team where id=#{id}
            """)
    String accept(Integer id);
    @Insert("""
            insert into team_member (team_id, player_id)
            select team_id, player_id
            from request_team
            where id=#{id}
            """)
    void  acceptMember(Integer id);

    @Select("""
            select player_id from team_member where team_id=#{team_id} and player_id=#{player_id}
            """)
    Integer joinedPlay(Integer team_id, Integer player_id);
}
