package com.kshrd.soccer_date.model;

import com.kshrd.soccer_date.controller.CommentMatchController;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    private Integer matchId;
    private String gameType;
    private Venue venue;
    private String pitch;
    private LocalDateTime time;
    private Team homeTeam;
    private Team awayTeam;
    private Integer scoreHome;
    private Integer scoreAway;
    private Integer like_id;
    private Integer likes;
    private Boolean attend;
    private LocalDateTime create_at;
//    private List<CommentMatch> comments;
    private Boolean is_owner;
}
