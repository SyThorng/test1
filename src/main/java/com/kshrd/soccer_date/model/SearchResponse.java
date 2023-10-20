package com.kshrd.soccer_date.model;

import com.kshrd.soccer_date.model.dto.UserAppDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    List<Team> teams;
    List<UserAppDTO> players;
    List<Venue> locations;

}
