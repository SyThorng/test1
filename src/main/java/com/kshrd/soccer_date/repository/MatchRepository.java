package com.kshrd.soccer_date.repository;

import com.kshrd.soccer_date.model.Match;
import com.kshrd.soccer_date.model.Team;
import com.kshrd.soccer_date.model.Venue;
import com.kshrd.soccer_date.model.request.MatchRequest;
import com.kshrd.soccer_date.model.request.MatchUpdateScoreRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MatchRepository {

    @Select("""
            SELECT *  from venue where id=#{id} 
            """)
    @Result(property = "venueId", column = "id")
    @Result(property = "venueName", column = "location_name")
    @Result(property = "venueLogo", column = "image")
    Venue searchVenueById(Integer id);

    @Select("""
            SELECT * FROM team where id=#{teamId}
            """)
    Team searchTeamById(Integer teamId);
@Select("""
        select count(match_id) from match_like where match_id=#{match_id}
        """)
    Integer countLike(Integer match_id);
    @Select("""
            SELECT * FROM match order by create_at DESC limit #{pageSize}
            """)
    @Results(id = "matchMap", value = {
            @Result(property = "matchId", column = "id"),
            @Result(property = "gameType", column = "match_title"),
            @Result(property = "venue", column = "location_id",
                    one = @One(select = "searchVenueById")),
            @Result(property = "homeTeam", column = "home_id",
                    one = @One(select = "com.kshrd.soccer_date.repository.TeamRepository.searchTeamById")
            ),
            @Result(property = "awayTeam", column = "away_id",
                    one = @One(select = "com.kshrd.soccer_date.repository.TeamRepository.searchTeamById")
            ),
            @Result(property = "scoreHome", column = "score_home"),
            @Result(property = "scoreAway", column = "score_away"),
            @Result(property = "likes",column = "id",one = @One(select = "countLike"))
//            @Result(property = "comments", column = "id",
//                    many = @Many(select = "com.kshrd.soccer_date.repository.CommentMatchRepository.searchCommentByMatchId")
//            ),
    })
    List<Match> getAllMatch(Integer pageSize);


    @Select("""
            INSERT INTO match(match_title, time, location_id, pitch, home_id)
            VALUES (#{match.gameType}, #{match.time}, #{match.location},#{match.pitch}, #{teamId})
            returning *
            """)
    @ResultMap("matchMap")
    Match createMatch(@Param("match") MatchRequest matchRequest, String email, Integer teamId);


    @Select("""
            DELETE FROM match where id=#{matchId} returning *
            """)
    @ResultMap("matchMap")
    Match deleteMatchById(Integer matchId);


    @Select("""
            SELECT * FROM match where id=#{matchId}
            """)
    @ResultMap("matchMap")
    Match searchMatchById(Integer matchId);


    @Select("""
            UPDATE  match SET  match_title=#{match.gameType},
                              time =#{match.time},
                              location_id=#{match.location},
                              pitch=#{match.pitch} 
                              where id=#{matchId} returning *
            """)
    @ResultMap("matchMap")
    Match updateMatchById(@Param("match") MatchRequest matchRequest, String email, Integer matchId);

    @Select("""
            SELECT home_id FROM match where id=#{matchId}
            """)
    Integer getHomeTeamIdByMatchId(Integer matchId);

    @Select("""
            SELECT away_id FROM match where id=#{matchId}
            """)
    Integer getawayTeamIdByMatchId(Integer matchId);


    @Select("""
            UPDATE  match SET 
                              score_home=#{match.scoreHome},
                              score_away=#{match.scoreAway}
                              where id=#{matchId} returning *
            """)
    @ResultMap("matchMap")
    Match updateScoreMatchByMatchId(@Param("match") MatchUpdateScoreRequest matchUpdateScoreRequest, Integer matchId);
    @Select("""
            select * from match 
            inner join request_match rm on match.id = rm.match_id inner join
            venue v on match.location_id = v.id where status='accept' and  home_id=#{team_id} OR away_id=#{team_id}
            """)
    @ResultMap("matchMap")
    //@Result(property = "venue",column = )
    List<Match> getApprovedTeamByTeamId(Integer team_id);

    @Select("""
            SELECT * FROM match where match_title =#{gametype}
            and (home_id=#{teamId} or away_id=#{teamId}) 
            """)
    @ResultMap("matchMap")
    List<Match> searchMatchByGameType(String gametype,Integer teamId);

    @Select("""
            SELECT * FROM match where home_id=#{teamId} or away_id =#{teamId}
            """)
    @ResultMap("matchMap")
    List<Match> searchMatchByTeamId(Integer teamId);

    @Select("""
            SELECT *
            from match
                     join venue v on match.location_id = v.id
                    where v.location_name ilike '%${locationName}%'
            """)
    @ResultMap("matchMap")
    List<Match> searchMatchByLocationName(String locationName);



    @Select("""
               select * from match inner join
             team t on match.home_id = t.id 
             or match.away_id = t.id inner join 
             team_member tm on t.id = tm.team_id 
             where tm.player_id=#{user_id} order by time asc 
            """)
    @ResultMap("matchMap")
    List<Match> getUpcomingMatchByTeamId(Integer user_id);

    @Select("""
            select id from match_attendee where match_id=#{match_id} and player_id=#{user_id}
            """)
    Integer getAttenId(Integer match_id,Integer user_id);

}
