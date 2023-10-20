package com.kshrd.soccer_date.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerScore {
    private Integer id;
    private Integer team_id;
    private Integer match_id;
    private Integer player_id;
    private String player_name;
    private Integer numberOfGoal;
}
