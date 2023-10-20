package com.kshrd.soccer_date.repository;

import com.kshrd.soccer_date.model.RequestMatch;
import com.kshrd.soccer_date.model.request.RequestMatchRequest;
import com.kshrd.soccer_date.model.request.TeamRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RequestMatchRepository {
    @Select("""
            Insert into request_match(team_id,match_id) values(#{req.team_id},#{req.match_id}) returning *
            """)
    RequestMatch addRequestMatch(@Param("req") RequestMatchRequest request);

    @Select("""
            select is_owner from team_member where player_id=#{user_id} and team_id=#{team_id}
            """)
    Boolean isOwner(Integer team_id, Integer user_id);

    @Select("""
             select request_match.id,match_id,home_id,request_match.team_id,status,request_match.create_at,time from request_match inner join match m on request_match.match_id = m.id
             inner join team_member tm on m.home_id= tm.team_id where is_owner=true and tm.player_id=#{userId} and status='pending' and home_id=#{home_id}
                                   """)
    @Result(property = "team", column = "team_id", one = @One(select = "com.kshrd.soccer_date.repository.TeamRepository.searchTeamById"))
    @Result(property = "time", column = "time")
    @Result(property = "homeTeam", column = "home_id", one = @One(select = "com.kshrd.soccer_date.repository.TeamRepository.searchTeamById"))
    List<RequestMatch> getAllRequestMatchByCurrentId(Integer userId,Integer home_id);

    @Select("""
            update request_match set status=#{status} where id=#{id}
            """)
    void updateStatusMatch(String status, Integer id);

    @Update("""
            update request_match set status='reject' where match_id=#{match_id} and status='pending'
            """)
    void updateStatusRejectMatch(Integer match_id);

    @Select("""
            select team_id from request_match where id=#{id}
            """)
    Integer AwayId(Integer id);

    @Update("""
            update match set away_id=#{team_id} where id=#{match_id}
            """)
    void updateMatch(Integer team_id, Integer match_id);


    @Select("""
            select * from request_match where id=#{id}
            """)
    RequestMatch searchRequestById(Integer id);

}
