package com.kshrd.soccer_date.controller;


import com.kshrd.soccer_date.jwt.JwtTokenUtil;
import com.kshrd.soccer_date.model.dto.UserAppDTO;
import com.kshrd.soccer_date.model.request.PasswordRequest;
import com.kshrd.soccer_date.model.request.UserLoginRequest;
import com.kshrd.soccer_date.model.request.UserRequest;
import com.kshrd.soccer_date.model.response.Response;
import com.kshrd.soccer_date.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userDetailsService;

    //
    //    ------------------------------  Login Account  -------------------------------------
    @PostMapping("/login")
    public ResponseEntity<Response<UserAppDTO>> Authentication(@RequestBody UserLoginRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);
        UserAppDTO userAppDTO = userDetailsService.getUserByEmail(authenticationRequest.getEmail());
        userAppDTO.setToken(token);
//        userAppDTO.setProfile("http://localhost:8080/api/v1/image/get-image?file=" + userAppDTO.getProfile());
        Response<UserAppDTO> response = Response.<UserAppDTO>builder()
                .message("Login success..!!")
                .payload(userAppDTO)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }


    //    ------------------------------  Register Account -------------------------------------
    @PostMapping("/register")
    public ResponseEntity<Response<UserAppDTO>> register(
            @RequestBody UserRequest userRequest
    ) {
        UserAppDTO userAppDTO = userDetailsService.register(userRequest);
        Response<UserAppDTO> response = Response.<UserAppDTO>builder()
                .message("Register success..!!")
                .payload(userAppDTO)
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    //    ------------------------------  Verify Code  -------------------------------------
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(
            @RequestParam String code,
            @RequestParam String email
    ) {
        String user = userDetailsService.verifyCode(code, email);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/forget-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody PasswordRequest passwordRequest,
            @RequestParam String email,
            @RequestParam String code
    ) {
        String newPassword = passwordRequest.getNewPassword();
        Response<String> response = Response.<String>builder()
                .message(userDetailsService.forgetPassword(newPassword, newPassword, email, code))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/verify-email")
    public String verifyEmail(
            @RequestParam("email") String email
    ) {
        return userDetailsService.verifyEmail(email);
    }

    @PostMapping("/login-facebook")
    public ResponseEntity<Response<UserAppDTO>> loginFacebook(
            @RequestBody UserRequest userRequest
    ) {
        Response<UserAppDTO> response = Response.<UserAppDTO>builder()
                .message("login facebook is success...!!")
                .payload(userDetailsService.loginFacebook(userRequest))
                .status(200)
                .build();
        return ResponseEntity.ok().body(response);
    }

    private void authenticate(String username, String password) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}


