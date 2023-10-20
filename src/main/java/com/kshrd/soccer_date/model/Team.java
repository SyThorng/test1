package com.kshrd.soccer_date.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.soccer_date.model.dto.UserAppDTO;
import com.kshrd.soccer_date.model.response.TeamMemberResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer teamId;
    private String name;
    private String logo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    Integer playerNumber;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    String role_play;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Boolean is_owner;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    UserAppDTO userOwner;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<TeamMemberResponse> members;

}
