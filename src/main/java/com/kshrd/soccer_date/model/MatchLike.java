package com.kshrd.soccer_date.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchLike {
    private Integer id;
    private Integer match_id;
    private Integer user_id;
    private LocalDateTime create_at;
}
