package com.kshrd.soccer_date.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    private Integer id;
    private Integer post_id;
    private Integer user_id;
    private LocalDateTime create_at;
}
