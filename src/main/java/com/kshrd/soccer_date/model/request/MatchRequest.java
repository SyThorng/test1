package com.kshrd.soccer_date.model.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequest {
    private String gameType;
    private Integer location;
    private String pitch;
    private LocalDateTime time;
}
