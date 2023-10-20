package com.kshrd.soccer_date.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class GlobalException  {

    @ExceptionHandler(FieldNotFoundException.class)
    public ProblemDetail fieldNotFound(
            FieldNotFoundException fieldNotFoundException
    ){
        ProblemDetail problemDetail=ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,fieldNotFoundException.getMessage()
        );
        problemDetail.setTitle(fieldNotFoundException.title);
        problemDetail.setType(URI.create("https://8080/errors/"));
        return problemDetail;
    }

    @ExceptionHandler(InvalidFieldException.class)
    public ProblemDetail invalidField(
            InvalidFieldException invalidFieldException
    ){
        ProblemDetail problemDetail=ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,invalidFieldException.getMessage()
        );
        problemDetail.setTitle(invalidFieldException.title);
        problemDetail.setType(URI.create("https://8080/errors/"));
        return problemDetail;
    }
    @ExceptionHandler(NotOwnerException.class)
    public ProblemDetail notOwner(
            NotOwnerException notOwnerException
    ){
        ProblemDetail problemDetail=ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,notOwnerException.getMessage()
        );
        problemDetail.setTitle(notOwnerException.getTitle());
        problemDetail.setType(URI.create("https://8080/errors/"));
        return problemDetail;
    }
    @ExceptionHandler(LikeException.class)
    public ProblemDetail AlreadyLike(
            LikeException likeException
    ){
        ProblemDetail problemDetail=ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,likeException.getMessage()
        );
        problemDetail.setTitle(likeException.title);
        problemDetail.setType(URI.create("https://8080/errors/"));
        return problemDetail;
    }
    @ExceptionHandler(ForbiddenException.class)
    public ProblemDetail Forbidden(
            ForbiddenException forbiddenException
    ){
        ProblemDetail problemDetail=ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,forbiddenException.getMessage()
        );
        problemDetail.setTitle(forbiddenException.getTitle());
        problemDetail.setType(URI.create("https://8080/errors/"));
        return problemDetail;
    }



    @ExceptionHandler(AttendanceException.class)
    public ProblemDetail AttendanceException(
            AttendanceException attendanceException
    ){
        ProblemDetail problemDetail=ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,attendanceException.getMessage()
        );
        problemDetail.setTitle(attendanceException.getTitle());
        problemDetail.setType(URI.create("https://8080/errors/"));
        return problemDetail;
    }

}
