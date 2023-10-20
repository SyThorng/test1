package com.kshrd.soccer_date.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.soccer_date.model.dto.UserAppDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentMatch {
    private Integer commentId;
    private String text;
    private Integer matchId;
    private UserAppDTO user;
    private Boolean is_public;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mention;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime create_at;
}
