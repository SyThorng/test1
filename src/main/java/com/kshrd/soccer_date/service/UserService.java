package com.kshrd.soccer_date.service;

import com.kshrd.soccer_date.model.dto.UserAppDTO;
import com.kshrd.soccer_date.model.request.*;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService{

    UserAppDTO getUserByEmail(String email);
    UserAppDTO register(UserRequest userRequest);
    String verifyCode(String code,String email);
    UserAppDTO searchUserByEmail(String id);

    String forgetPassword(String newPassword, String confirmPassword, String email, String code);
    String verifyEmail(String email);

    UserAppDTO updateInformationUser(UserUpdateRequest userUpdateRequest, String fileName, String userId);

    String resetPasswordByCurrentUser(PasswordRequestReset passwordRequestReset, String email);

    UserAppDTO setUpProfile(UserSetupRequest userSetupRequest,String email);

    List<UserAppDTO> getAllUser(Integer pageSize,Integer pageNo);

    List<UserAppDTO> searchUserByName(String userName,Integer pageSize,Integer pageNo);
    UserAppDTO searchUserById(Integer userId);

    UserAppDTO updateIsSkipByCurrentUser(String email);
    UserAppDTO loginFacebook(UserRequest userRequest);
}
