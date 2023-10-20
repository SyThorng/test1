package com.kshrd.soccer_date.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitePlayerRequest {
    private Integer team_id;
    private String email;
}
