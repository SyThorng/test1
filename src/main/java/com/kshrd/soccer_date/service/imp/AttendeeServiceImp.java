package com.kshrd.soccer_date.service.imp;
import com.kshrd.soccer_date.exception.AttendanceException;
import com.kshrd.soccer_date.exception.FieldNotFoundException;
import com.kshrd.soccer_date.exception.NotOwnerException;
import com.kshrd.soccer_date.model.Attendee;
import com.kshrd.soccer_date.model.request.AttendeeRequest;
import com.kshrd.soccer_date.repository.AttendeeRepository;
import com.kshrd.soccer_date.repository.ChatRepository;
import com.kshrd.soccer_date.service.AttendeeService;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendeeServiceImp implements AttendeeService {
    private final ChatRepository chatRepository;
    private final AttendeeRepository repository;

    public AttendeeServiceImp(ChatRepository chatRepository, AttendeeRepository repository) {
        this.chatRepository = chatRepository;
        this.repository = repository;
    }

    @Override
    public Attendee checkAttendance(AttendeeRequest request, Integer player_id) {
        Attendee attendee = repository.checkedAttendance(request.getMatch_id(), player_id);
        Integer member = chatRepository.member(player_id, request.getTeam_id());
        if (member == null) {
            throw new NotOwnerException("You are not team member", "Not Member!");
        } else if (attendee != null) {
            throw new AttendanceException("Already joined match", "You are checked for going to match!");
        }
        return repository.checkAttendance(request, player_id);
    }

    @Override
    public List<Attendee> attendanceByMatchId(Integer match_id,Integer team_id) {
        return repository.attendanceByMatchId(match_id,team_id);
    }
    @Override
    public void cancelPlay(Integer id, Integer player_id) {
        Integer deleted = repository.cancelPlay(id, player_id);
        Attendee attendee = repository.searchAttendanceById(id);
        if (attendee == null) {
            throw new FieldNotFoundException("Not Found!","This attendance is not found!");
        } else if (deleted != 1) {
            throw new NotOwnerException("You are not owner!", "Not Owner");
        }
    }

    @Override
    public Attendee attendanceByMatchIdCurrentId(Integer id, Integer teamId, Integer user_id) {
        return repository.getAttendenceCurrentUser(id,teamId,user_id);
    }

}
