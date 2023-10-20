package com.kshrd.soccer_date.model;
import com.kshrd.soccer_date.model.dto.UserAppDTO;
import com.kshrd.soccer_date.model.response.TeamMemberResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTeam {
    private Integer id;
    private Integer team_id;
    private Integer player_id;
    private TeamMemberResponse player;
    private String status;
    private LocalDateTime create_at;
}
