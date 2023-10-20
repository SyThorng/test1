package com.kshrd.soccer_date.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Integer id;
    private Integer post_id;
    private Integer user_id;
    private String username;
    private String profile;
    private String comment;
    private LocalDateTime create_at;
}
