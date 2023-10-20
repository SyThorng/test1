package com.kshrd.soccer_date.service.imp;

import com.kshrd.soccer_date.config.PasswordEncoderConfig;
import com.kshrd.soccer_date.enums.ERolePlayer;
import com.kshrd.soccer_date.exception.FieldNotFoundException;
import com.kshrd.soccer_date.exception.InvalidFieldException;
import com.kshrd.soccer_date.mapper.UserMapper;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.dto.UserAppDTO;
import com.kshrd.soccer_date.model.request.*;
import com.kshrd.soccer_date.repository.UserRepository;
import com.kshrd.soccer_date.service.EmailService;
import com.kshrd.soccer_date.service.FileUploadFileService;
import com.kshrd.soccer_date.service.UserService;

import java.lang.Integer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

//sythorngv4
@Service
public class UserServiceImp implements UserService {
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final EmailService emailService;

    private final UserRepository userRepository;

    private final FileUploadFileService uploadFileService;
    //for resendCode need userID


    public UserServiceImp(PasswordEncoderConfig passwordEncoderConfig, EmailService emailService, UserRepository userRepository, FileUploadFileService uploadFileService) {
        this.passwordEncoderConfig = passwordEncoderConfig;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.uploadFileService = uploadFileService;
    }

//    Integer ID = 0;
//    static UserRequest userRequestRandomCode = null;

    @Value("${image.url}")
    private String imageUrl;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Integer userId = userRepository.getUserIdByEmail(email);
        if (userRepository.getUserByEmail(email) == null) {
            throw new FieldNotFoundException(
                    "Email Invalid",
                    "Email Not Found...!"
            );
        }
        if (!userRepository.isVerifyUser(userId)) {
            throw new InvalidFieldException(
                    "Invalid login",
                    "User Not yet Verify code for login,please go to verify code or register again"
            );
        }
        return userRepository.getUserByEmail(email);
    }

    @Override
    public UserAppDTO getUserByEmail(String email) {
        UserApp userApp = userRepository.getUserByEmail(email);
        userApp.setProfile(imageUrl + userApp.getProfile());
        return UserMapper.INSTANCE.toUserAppDTO(userApp);
    }

    @Override
    public UserAppDTO register(UserRequest userRequest) {
        //email wait for handle ft validate
        if (!userRequest.getEmail().matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,6}")) {
            throw new InvalidFieldException(
                    "Email invalid",
                    "Email should be like this -> somthing@somthing.com"
            );
        }

        //validate password
        if (!userRequest.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            throw new InvalidFieldException(
                    "Invalid password",
                    "Password should be at least 8 character and 1 special character Uppercase and Lowercase character and No Space"
            );
        }

        //validate ConfirmPassword
//        if (!userRequest.getConfirmPassword().equals(userRequest.getPassword())) {
//            throw new InvalidFieldException(
//                    "Invalid password",
//                    "ConfirmPassword is wrong with password"
//            );
//        }

        //bcript password
        userRequest.setPassword(passwordEncoderConfig.passwordEncoder().encode(userRequest.getPassword()));

        if (userRepository.searchUserByEmail(
                userRequest.getEmail()) != null &&
                userRepository.findIsVerifyByEmail(userRequest.getEmail()) == true
        ) {
            throw new InvalidFieldException(
                    "User Invalid",
                    "email already register"
            );
        }

        if (userRepository.searchUserByEmail(
                userRequest.getEmail()) != null &&
                !userRepository.findIsVerifyByEmail(userRequest.getEmail())
        ) {
            userRepository.deleteUserByEmailUser(userRequest.getEmail());
        }

        //get userId
        Integer userId = userRepository.register(userRequest);

        userRepository.addUserIdToUserDetail(userId, 1);

        //random code
        Random rand = new Random();
        int randomNumber1 = rand.nextInt(900000) + 100000;

        String randomNumber = String.valueOf(randomNumber1);

        //add random code to table
        userRepository.verifyCodeToUser(userId, randomNumber);

        //verify code user
        if (!userRepository.isVerifyUser(userId)) {
            emailService.sendSimpleMail(userRepository.searchUserById(userId), randomNumber);
        }

        UserApp userApp = userRepository.searchUserById(userId);
        userApp.setProfile(imageUrl + userApp.getProfile());
        return UserMapper.INSTANCE.toUserAppDTO(userApp);
    }


    @Override
    public String verifyCode(String code, String email) {

        String code2 = code.trim();
        String Code = userRepository.searchVerifyByCode(code2);
//
        if (Code == null) {
            throw new FieldNotFoundException(
                    "Code Invalid ",
                    "Code Not Found..!!"
            );
        }

        Integer userId = userRepository.getUserIdByEmail(email);

        if (userRepository.searchVerifyCodeByUserId(userId, code) == null) {
            throw new InvalidFieldException(
                    "Invalid",
                    "you not yet verify email please go to verify"
            );
        }

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = formatter.format(now);

        LocalTime time1 = LocalTime.parse(formattedTime);
        LocalTime time2 = LocalTime.parse(userRepository.findExpiredDateUserByCode(code2));

        Duration duration = Duration.between(time2, time1);

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

//        System.out.println(hours + ":" + minutes + "" + ":" + seconds);

        //expired 2 minute
        if (minutes >= 2) {
            userRepository.deleteCodeVerify(code2);
            return "Code is expired...!!";
        }

        if (userId != null) {
            userRepository.updateStatusByUserId(userId);
            userRepository.deleteAllVerify(userId);
        } else {
            return "Code is not Complete...!!";
        }
        return "verify code Success !! ";
    }

    @Override
    public UserAppDTO searchUserByEmail(String email) {
        UserApp userApp = userRepository.getUserByEmail(email);
        userApp.setProfile(imageUrl + userApp.getProfile());
        return UserMapper.INSTANCE.toUserAppDTO(userApp);
    }




    static Integer restId = 0;

    @Override
    public String verifyEmail(String email) {
        UserApp userApp = userRepository.searchUserByEmail(email);
        if (userApp == null) {
            throw new FieldNotFoundException(
                    "Email invalid",
                    "Email not Found..!"
            );
        }
        userRepository.deleteAllVerify(userApp.getUserId());
        restId = userApp.getUserId();
        Random rand = new Random();
        int randomNumber1 = rand.nextInt(900000) + 100000;

        String randomNumber = String.valueOf(randomNumber1);

        //add random code to table
        userRepository.verifyCodeToUser(userApp.getUserId(), randomNumber);
        //send email
        emailService.sendSimpleMailForResetPassword(userApp, randomNumber);

        return "Verify email success..!!";
    }


    @Override
    public String forgetPassword(String newPassword, String confirmPassword, String email, String code) {

        Integer userId = userRepository.getUserIdByEmail(email);
        UserApp userApp = userRepository.searchUserByEmail(email);


        if (userApp == null) {
            throw new FieldNotFoundException(
                    "Email invalid",
                    "Email not Found..!"
            );
        }

        if(userRepository.searchVerifyByCode(code)==null){
            throw new FieldNotFoundException(
                    "Not Found",
                    "Code Not Found..!!"
            );
        }

        if (userRepository.searchVerifyCodeByUserId(userId, code) == null) {
            throw new InvalidFieldException(
                    "Invalid",
                    "you not yet verify email please go to verify"
            );
        }


//        if (userId == null) {
//            throw new InvalidFieldException(
//                    "Invalid Field",
//                    "Cannot Change Password please go to verify email to get code "
//            );
//        }

        //validate password
        if (!newPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            throw new InvalidFieldException(
                    "Invalid password",
                    "Password should be at least 8 character and 1 special character Uppercase and Lowercase character and No Space"
            );
        }


        String password = passwordEncoderConfig.passwordEncoder().encode(newPassword);
        userRepository.updatePasswordByUserID(userId, password);

        userRepository.deleteAllVerify(userId);
        return "update Password Success....!!!";
    }


    @Override
    public UserAppDTO updateInformationUser(UserUpdateRequest userUpdateRequest, String fileName, String Email) {
        Integer userId = userRepository.getUserIdByEmail(Email);
        Boolean is_role = false;

//        if (userUpdateRequest.getAddress().isEmpty()) {
//            userUpdateRequest.setAddress("Phnom Penh");
//        }
//        if (fileName == "") {
//            fileName = userRepository.getProfileImageByEmail(Email);
//        }
//
//        if (userUpdateRequest.getFirstName().isEmpty()) {
//            userUpdateRequest.setFirstName(userRepository.getFirstNameByUserId(userId));
//        }
//
//        if (userUpdateRequest.getLastName().isEmpty()) {
//            userUpdateRequest.setLastName(userRepository.getLastNameByUserId(userId));
//        }

        userUpdateRequest.setSkill(userUpdateRequest.getSkill().toUpperCase());
        userUpdateRequest.setAddress(userUpdateRequest.getAddress().toUpperCase());

        for (ERolePlayer rolePlayer : ERolePlayer.values()) {
            if (userUpdateRequest.getSkill().equals(rolePlayer.name())) {
                is_role = true;
                break;
            }
        }

        if (!is_role) {
            throw new InvalidFieldException(
                    "Invalid Skill",
                    "This skill is not correct : " +
                            "please input one of (GK,RB,CB,LB,CMF,DMF,LWF,RWF,CF)");
        }
        if (!userUpdateRequest.getNumber().matches("^\\d{1,3}$")) {
            throw new InvalidFieldException(
                    " number is valid",
                    "you can input 1-3 number -> 123"
            );
        }

        if (userUpdateRequest.getContact() == "") {
            userUpdateRequest.setContact(null);
        }

        if (userUpdateRequest.getContact() != null) {
            if (!userUpdateRequest.getContact().matches("^0\\d{8,9}$")) {
                throw new InvalidFieldException(
                        "Phone number is valid",
                        "contact should be like this -> 0883763512"
                );
            }
        }


        UserApp userApp = userRepository.updateInformationUser(userUpdateRequest, fileName, Email);
        userApp.setProfile(imageUrl + fileName);
        return UserMapper.INSTANCE.toUserAppDTO(userApp);
    }

    @Override
    public String resetPasswordByCurrentUser(PasswordRequestReset passwordRequestReset, String email) {

        Integer userId = userRepository.getUserIdByEmail(email);

        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        String old = userRepository.getPasswordByUserId(userId);


        if (!b.matches(passwordRequestReset.getOldPassword(), old)) {
            throw new InvalidFieldException(
                    "Invalid password",
                    "Old Password you input is wrong with your old password "
            );
        }

//        validate password
        if (!passwordRequestReset.getNewPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            throw new InvalidFieldException(
                    "Invalid password",
                    "Password should be at least 8 character and 1 special character Uppercase and Lowercase character and No Space"
            );
        }

        //validate ConfirmPassword
//        if (!passwordRequest.getConfirmPassword().equals(passwordRequest.getNewPassword())) {
//            throw new InvalidFieldException(
//                    "Invalid password",
//                    "ConfirmPassword is wrong with password"
//            );
//        }
//
        String password = passwordEncoderConfig.passwordEncoder().encode(passwordRequestReset.getNewPassword());
        userRepository.updatePasswordByUserID(userId, password);

        return "update Password Success....!!!";
    }

    @Override
    public UserAppDTO setUpProfile(UserSetupRequest userSetupRequest, String email) {

        Boolean is_role = false;

//        if (userSetupRequest.getImage() == "") {
//            userSetupRequest.setImage("defaultProfile.png");
//        }
//        if (userSetupRequest.getAddress() == "") {
//            userSetupRequest.setAddress("Phnom Penh");
//        }

        userSetupRequest.setSkill(userSetupRequest.getSkill().toUpperCase());
        userSetupRequest.setAddress(userSetupRequest.getAddress().toUpperCase());

        for (ERolePlayer rolePlayer : ERolePlayer.values()) {
            if (userSetupRequest.getSkill().equals(rolePlayer.name())) {
                is_role = true;
                break;
            }
        }

        if (!is_role) {
            throw new InvalidFieldException(
                    "Invalid Skill",
                    "This skill is not correct : " +
                            "please input one of (GK,RB,CB,LB,CMF,DMF,LWF,RWF,CF)");
        }

        if (!userSetupRequest.getNumber().matches("^\\d{1,3}$")) {
            throw new InvalidFieldException(
                    " number is valid",
                    "you can input 1-3 number -> 123"

            );
        }

        if (userSetupRequest.getContact() == "") {
            userSetupRequest.setContact(null);
        }


        if (userSetupRequest.getContact() != null) {
            if (!userSetupRequest.getContact().matches("^0\\d{8,9}$")) {
                throw new InvalidFieldException(
                        "Phone number is valid",
                        "contact should be like this -> 088376351"
                );
            }
        }


        UserApp userApp = userRepository.setUpProfile(userSetupRequest, email);
        userApp.setProfile(imageUrl + userSetupRequest.getImage());

        return UserMapper.INSTANCE.toUserAppDTO(userApp);
    }

    @Override
    public List<UserAppDTO> getAllUser(Integer pageSize, Integer pageNo) {
        List<UserAppDTO> userApp = userRepository.getAllUser(pageSize, pageNo).stream().map(UserMapper.INSTANCE::toUserAppDTO).toList();
        userApp.forEach(e -> e.setProfile(imageUrl + e.getProfile()));
        return userApp;
    }

    @Override
    public List<UserAppDTO> searchUserByName(String userName, Integer pageSize, Integer pageNo) {

        List<UserAppDTO> userAppDTOS = userRepository.searchUserByName(userName.replace(" ", ""), pageSize, pageNo).stream().map(UserMapper.INSTANCE::toUserAppDTO).toList();
        if (userAppDTOS.size() == 0) {
            throw new FieldNotFoundException(
                    "User Not Found",
                    "User Name [" + userName + "] is not found...!!"
            );
        }

        userAppDTOS.forEach(e -> e.setProfile(imageUrl + e.getProfile()));
        return userAppDTOS;
    }

    @Override
    public UserAppDTO searchUserById(Integer userId) {
        UserApp userApp = userRepository.searchUserById(userId);
        if (userApp == null) {
            throw new FieldNotFoundException(
                    "User Not Found",
                    "User id [" + userId + "] is not found...!!"
            );
        }
        userApp.setProfile(imageUrl + userApp.getProfile());
        return UserMapper.INSTANCE.toUserAppDTO(userApp);
    }

    @Override
    public UserAppDTO updateIsSkipByCurrentUser(String email) {
        Integer userId = userRepository.getUserIdByEmail(email);
        UserApp userApp = userRepository.updateIsSkipByCurrentUser(userId);
        return UserMapper.INSTANCE.toUserAppDTO(userApp);
    }

    @Override
    public UserAppDTO loginFacebook(UserRequest userRequest) {

        if (userRepository.searchUserByEmail(
                userRequest.getEmail()) != null
        ) {
            throw new InvalidFieldException(
                    "User Invalid",
                    "email already register"
            );
        }
        userRequest.setPassword(passwordEncoderConfig.passwordEncoder().encode(userRequest.getPassword()));

        Integer userId =userRepository.register(userRequest);

        UserApp userApp=userRepository.searchUserById(userId);
        userApp.setProfile(imageUrl + userApp.getProfile());

        userRepository.updateStatusByUserId(userId);

        return UserMapper.INSTANCE.toUserAppDTO(userApp);
    }
}
