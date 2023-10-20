package com.kshrd.soccer_date.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private Integer id;
    private Integer user_id;
    private String username;
    private String profile_image;
    private String caption;
    private String url;
    private Integer like_id;
    private Integer likes;
    private LocalDateTime create_at;
}
