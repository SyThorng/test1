package com.kshrd.soccer_date.controller;
import com.kshrd.soccer_date.model.Attendee;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.request.AttendeeRequest;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.AttendeeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("api/v1")
@SecurityRequirement(name = "auth")
public class AttendeeController {
    private final AttendeeService service;

    public AttendeeController(AttendeeService service) {
        this.service = service;
    }

    private static Integer currentId() {
        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUserId();
    }
    @PostMapping("/attendee")
    public ResponseEntity<Response<Attendee>> checkAttendance(@RequestBody AttendeeRequest request) {
        Response<Attendee> response = Response.<Attendee>builder()
                .message("Attendance has been checked successfully..!!")
                .payload(service.checkAttendance(request,currentId()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/attendee/{matchId}/{teamId}")
    public ResponseEntity<Response<List<Attendee>>> attendanceByMatchId(@PathVariable("matchId") Integer id,@PathVariable("teamId")Integer team_id) {
        Response<List<Attendee>> response = Response.<List<Attendee>>builder()
                .message("Fetch all attendances successfully..!!")
                .payload(service.attendanceByMatchId(id,team_id))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/attendee/{matchId}/{teamId}/user")
    public ResponseEntity<Response<Attendee>> attendanceByMatchIdAndCurrentId(@PathVariable("matchId") Integer id,@PathVariable("teamId")Integer team_id) {
        Response<Attendee> response = Response.<Attendee>builder()
                .message("Fetch your attendance successfully..!!")
                .payload(service.attendanceByMatchIdCurrentId(id,team_id,currentId()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/attendee/{id}")
    public ResponseEntity<Response<Attendee>> cancelAttendance(@PathVariable Integer id) {
        service.cancelPlay(id,currentId());
        Response<Attendee> response = Response.<Attendee>builder()
                .message("Attendance has been cancel successfully..!!")
                .payload(null)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
