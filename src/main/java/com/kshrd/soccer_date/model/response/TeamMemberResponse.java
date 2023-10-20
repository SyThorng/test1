package com.kshrd.soccer_date.model.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberResponse {

    private Integer player_id;
    private String firstName;
    //    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String profile;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer playerNumber;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String rolePlay;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contact;
    private String skill;

}

