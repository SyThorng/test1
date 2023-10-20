package com.kshrd.soccer_date.repository;
import com.kshrd.soccer_date.model.Attendee;
import com.kshrd.soccer_date.model.request.AttendeeRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AttendeeRepository {
    @Select("""
            insert into match_attendee(team_id, match_id, player_id) 
            values (#{att.team_id},#{att.match_id},#{player_id}) returning *
            """)
    @Results(id = "attId",
            value = {
                    @Result(property = "player_id", column = "player_id"),
                    @Result(property = "player_name", column = "player_id", one = @One(select = "com.kshrd.soccer_date.repository.PostRepository.userName"))
            }
    )
    Attendee checkAttendance(@Param("att") AttendeeRequest request, Integer player_id);
    @Select("""
            select * from match_attendee where match_id=#{match_id} and player_id=#{player_id}
            """)
    Attendee checkedAttendance(Integer match_id, Integer player_id);
    @Select("""
            select * from match_attendee where id=#{id}
            """)
    Attendee searchAttendanceById(Integer id);
    @Delete("""
            delete from match_attendee where id=#{id} and player_id=#{user_id}
            """)
    Integer cancelPlay(Integer id,Integer user_id);
    @Select("""
            select * from match_attendee where match_id=#{match_id} and team_id=#{team_id}
            """)
    @ResultMap("attId")
    List<Attendee>attendanceByMatchId(Integer match_id, Integer team_id);

    @Select("""
            select * from match_attendee where match_id=#{id} and team_id=#{teamId} and player_id=#{userId}
            """)
    @ResultMap("attId")
    Attendee getAttendenceCurrentUser(Integer id, Integer teamId, Integer userId);
}
