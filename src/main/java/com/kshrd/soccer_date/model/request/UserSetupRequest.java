package com.kshrd.soccer_date.model.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSetupRequest {
    private String image;
    private String skill;
    private String address;
    private String number;
    private String contact;
}
