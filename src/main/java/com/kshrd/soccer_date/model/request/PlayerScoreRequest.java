package com.kshrd.soccer_date.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerScoreRequest {
    private Integer team_id;
    private Integer match_id;
    private Integer player_id;
}
