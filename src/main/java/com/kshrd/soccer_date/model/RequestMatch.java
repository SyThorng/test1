package com.kshrd.soccer_date.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMatch {
    private Integer id;
    private Integer match_id;
    private Team homeTeam;
    private Team team;
    private String status;
    private LocalDateTime time;
    private LocalDateTime create_at;
}
