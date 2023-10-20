package com.kshrd.soccer_date.repository;

import com.kshrd.soccer_date.model.PlayerScore;
import com.kshrd.soccer_date.model.request.PlayerScoreRequest;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlayerScoreRepository {
    @Select("""
            insert into player_score(match_id, team_id, player_id)
            values(#{pl.match_id},#{pl.team_id},#{pl.player_id}) returning *
            """)
    @Results(id = "scoreId", value = {
            @Result(property = "match_id", column = "match_id"),
            @Result(property = "player_id", column = "player_id"),
            @Result(property = "player_name", column = "player_id", one = @One(select = "com.kshrd.soccer_date.repository.PostRepository.userName")),
            @Result(property = "numberOfGoal", column = "gaol")
    })
    PlayerScore addScore(@Param("pl") PlayerScoreRequest request);

    @Select("""
            select team_id ,player_id,match_id,count(player_id) as gaol from player_score where match_id=#{match_id} and player_id=#{player_id}
            group by player_id, team_id,match_id
            """)
    @ResultMap("scoreId")
   PlayerScore getPlayerByPlayerIdAndMatchId(Integer player_id,Integer match_id);
    @Select("""
            select id from team_member where  player_id=#{player_id} and team_id=#{team_id}
            """)
    Integer checkPlayerIntTeam(Integer team_id, Integer player_id);

    @Select("""
            select player_id,match_id,team_id,count(player_id) as gaol from player_score where  match_id=#{match_id} and team_id=#{team_id} group by player_id, match_id, team_id
                """)
    @Result(property = "player_id", column = "player_id")
    @Result(property = "player_name",column = "player_id", one = @One(select = "com.kshrd.soccer_date.repository.PostRepository.userName"))
    @Result(property = "numberOfGoal",column = "gaol")
    List<PlayerScore> getScore(Integer match_id, Integer team_id);
}
