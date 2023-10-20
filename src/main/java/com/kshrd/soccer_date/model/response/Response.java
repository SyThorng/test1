package com.kshrd.soccer_date.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response<T> {
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    T payload;
    Integer status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    T team;
}
