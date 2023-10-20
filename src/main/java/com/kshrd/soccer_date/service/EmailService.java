package com.kshrd.soccer_date.service;

import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.request.UserRequest;

public interface EmailService {
    String sendSimpleMail(UserApp userRequest, String code);
    String sendSimpleMailForResetPassword(UserApp userApp, String code);
}
