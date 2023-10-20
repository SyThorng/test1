package com.kshrd.soccer_date.model.response;

import com.kshrd.soccer_date.model.Match;
import com.kshrd.soccer_date.model.UserApp;
import lombok.Data;

import java.util.List;

@Data
public class SearchResultResponse {

    private List<UserApp> userAppList;

    private List<Match> matchList;


}
