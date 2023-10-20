package com.kshrd.soccer_date.service;

import com.kshrd.soccer_date.model.Attendee;
import com.kshrd.soccer_date.model.request.AttendeeRequest;

import java.util.List;

public interface AttendeeService {
    Attendee checkAttendance(AttendeeRequest request,Integer player_id);
    List<Attendee>attendanceByMatchId(Integer match_id,Integer team_id);
    void cancelPlay(Integer id,Integer player_id);

    Attendee attendanceByMatchIdCurrentId(Integer id, Integer teamId, Integer integer);
}
