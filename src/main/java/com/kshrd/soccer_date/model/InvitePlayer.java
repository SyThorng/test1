package com.kshrd.soccer_date.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitePlayer {
    private Integer id;
    private Integer team_id;
    private Team team;
    private Integer player_id;
    private String player_name;
    private Integer inviter_id;
    private String inviter_name;
    private String status;
    private LocalDateTime create_at;
}
