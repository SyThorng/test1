package com.kshrd.soccer_date.repository;

import com.kshrd.soccer_date.model.InvitePlayer;
import com.kshrd.soccer_date.model.request.InvitePlayerRequest;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InvitePlayerRepository {
    @Select("""
            insert into invite_player(team_id, player_id, inviter_id) values(#{team_id},#{player_id},#{userId}) returning *
              """)
    @Results(id = "inviteId",value = {
            @Result(property = "player_id",column = "player_id"),
            @Result(property = "team_id",column = "team_id"),
            @Result(property = "inviter_id",column = "inviter_id"),
            @Result(property = "player_name",column = "player_id",one = @One(select = "com.kshrd.soccer_date.repository.PostRepository.userName")),
            @Result(property = "team",column = "team_id",one = @One(select = "com.kshrd.soccer_date.repository.TeamRepository.searchTeamById")),
            @Result(property = "inviter_name",column = "inviter_id",one = @One(select = "com.kshrd.soccer_date.repository.PostRepository.userName"))

    })

    InvitePlayer invitePlayer(Integer team_id,Integer player_id,Integer userId);
    @Select("""
            select id from app_user where email=#{email}
            """)
    Integer getPlayerIdByEmail(String email);

    @Select("""
            select * from invite_player where player_id=#{id} and status='pending'
            """)
    @ResultMap("inviteId")
    List<InvitePlayer> getInviterByCurrentUser(Integer id);

    @Select("""
            update invite_player set status=#{status} where id=#{id} returning *
             """)
    @ResultMap("inviteId")
    InvitePlayer acceptTeamInvite(String status, Integer id);

    @Select("""
            insert into team_member(team_id, player_id) select team_id,player_id from invite_player where id=#{id}
            """)
    void addPlayerToTeam(Integer id);

}

