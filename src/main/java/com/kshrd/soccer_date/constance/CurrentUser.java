package com.kshrd.soccer_date.constance;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class CurrentUser {
//    private static Integer currentId() {
//        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return user.getUserId();
//    }
//    public static  Integer USER_ID=currentId();

    private static String getUsernameOfCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public static  String USER_IDD = getUsernameOfCurrentUser();


}


