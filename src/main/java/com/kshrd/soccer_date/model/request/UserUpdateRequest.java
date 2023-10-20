package com.kshrd.soccer_date.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequest {
    private String image;
    private String firstName;
    private String lastName;
    private String skill;
    private String address;
    private String number;
    private String contact;
}
