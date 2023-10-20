package com.kshrd.soccer_date.controller;
import com.kshrd.soccer_date.model.dto.UserAppDTO;
import com.kshrd.soccer_date.model.request.PasswordRequestReset;
import com.kshrd.soccer_date.model.request.UserRequest;
import com.kshrd.soccer_date.model.request.UserSetupRequest;
import com.kshrd.soccer_date.model.request.UserUpdateRequest;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@SecurityRequirement(name = "auth")
@RestController
@RequestMapping("/api/v1/user")
public class UserAppController {


    private final UserService userService;

    public UserAppController(UserService userService) {
        this.userService = userService;
    }


    //    ------------------------------  Search User By ID  -------------------------------------
    @GetMapping("")
    public ResponseEntity<Response<UserAppDTO>> getCurrentUser(
    ) {
        Response<UserAppDTO> response = Response.<UserAppDTO>builder()
                .message("User found...!!")
                .payload(userService.searchUserByEmail(getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }


    String getUsernameOfCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }


    //    ------------------------------  Update Profile images  -------------------------------------


    @PutMapping(
            value = "/edit-profile"
    )

    public ResponseEntity<Response<UserAppDTO>> updateInformationUser(
            @RequestBody UserUpdateRequest userUpdateRequest
    ) {
        Response<UserAppDTO> response = Response.<UserAppDTO>builder()
                .message("Update information user Success...!!!")
                .payload(userService.updateInformationUser(userUpdateRequest, userUpdateRequest.getImage(), getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }


    @PutMapping("/reset-password-by-current-user")
    public ResponseEntity<?> resetPasswordByCurrentUser(
            @RequestBody PasswordRequestReset passwordRequestReset
    ){
        Response<String> response = Response.<String>builder()
                .message(userService.resetPasswordByCurrentUser(passwordRequestReset,getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(
            value = "/setup-profile"
    )
    public ResponseEntity<Response<UserAppDTO>> setupProfile(
            @RequestBody UserSetupRequest userSetupRequest
    ) {
        Response<UserAppDTO> response = Response.<UserAppDTO>builder()
                .message("SetUp information user Success...!!!")
                .payload(userService.setUpProfile(userSetupRequest,getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-all-user")
    public ResponseEntity<Response<List<UserAppDTO>>> getAllUser(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize
    ){
        Response<List<UserAppDTO>> response=Response.<List<UserAppDTO>>builder()
                .message("get all Match Success..!!")
                .payload(userService.getAllUser(pageSize,pageNo))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/user-name")
    public ResponseEntity<Response<List<UserAppDTO>>> searchUserByName(
            @RequestParam String userName,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize
    ){
        Response<List<UserAppDTO>> response=Response.<List<UserAppDTO>>builder()
                .message("get User Name  Success..!!")
                .payload(userService.searchUserByName(userName,pageSize,pageNo))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{userId}/user")
    @Operation(summary = "Search User By Id")
    public ResponseEntity<Response<UserAppDTO>> searchUserById(
            @PathVariable Integer userId
    ) {
        Response<UserAppDTO> response = Response.<UserAppDTO>builder()
                .message("get user By id Success...!!!")
                .payload(userService.searchUserById(userId))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }


    @PutMapping("/is_skip")
    public ResponseEntity<Response<UserAppDTO>> updateIsSkipByCurrentUser() {
        Response<UserAppDTO> response = Response.<UserAppDTO>builder()
                .message("update is_skip by current user is Success...!!!")
                .payload(userService.updateIsSkipByCurrentUser(getUsernameOfCurrentUser()))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }
}