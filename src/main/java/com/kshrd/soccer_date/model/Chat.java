package com.kshrd.soccer_date.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    private Integer id;
    private Integer team_id;
    private Integer player_id;
    private String username;
    private String message;
    private LocalDateTime create_at;
}
