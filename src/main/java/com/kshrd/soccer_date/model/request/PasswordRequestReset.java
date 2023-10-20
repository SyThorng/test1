package com.kshrd.soccer_date.model.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequestReset {
    private String oldPassword;
    private String newPassword;
}
