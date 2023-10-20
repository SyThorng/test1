package com.kshrd.soccer_date.model.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAppDTO {
    //    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer userId;
    private String firstName;
    //    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String profile;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
    private String skill;
    private String address;
    private String number;
    private String contact;
    private Boolean is_skip;
    private LocalDateTime create_at;
}
