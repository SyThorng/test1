package com.kshrd.soccer_date.service.imp;


import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.request.UserRequest;
import com.kshrd.soccer_date.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String sendSimpleMail(UserApp userRequest,String code) {

        try {

            String subject =
                    "Email Verification";
            String senderName
                    =
                    "Soccer Date";
            String html = "<!doctype html>\n" +
                    "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                    "      xmlns:th=\"http://www.thymeleaf.org\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\"\n" +
                    "          content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                    "    <title>Email</title>\n" +
                    "</head>\n" +
                    "<body>\n" +

                    "<p> Hi, "+ userRequest.getFirstName()+" "+userRequest.getLastName()+ "</p>"+
                    "<p>Thank you for registering with us,"+""+
                    "Please, verify your account with the code  below to complete your registration.</p>"+
                    "<div> <h2>" + code + "</h2></div>\n" +
                    "<p> Thank you <br> Users Registration Portal Service"+
                    "</body>\n" +
                    "</html>\n";

            MimeMessage message = javaMailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper (message) ;

//            FileSystemResource fileSystem=new FileSystemResource(new File("/Users/ski/Desktop/all-project/soccer_date/src/main/resources/images/logo.svg"));
//            messageHelper.addAttachment(fileSystem.getFilename(),fileSystem);

            messageHelper.setFrom( "sunsythorng9999@gmail.com", senderName);
            messageHelper.setTo(userRequest.getEmail());
            messageHelper.setSubject(subject);
            messageHelper.setText(html,true) ;
            javaMailSender.send (message) ;

            return "Mail Successfully...";
        } catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    @Override
    public String sendSimpleMailForResetPassword(UserApp userApp,String code) {
        try {

            String subject =
                    "Email Verification";
            String senderName
                    =
                    "Soccer Date";
            String html = "<!doctype html>\n" +
                    "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                    "      xmlns:th=\"http://www.thymeleaf.org\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\"\n" +
                    "          content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                    "    <title>Email</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<p> Hi, "+ userApp.getFirstName()+" "+userApp.getLastName()+ "</p>"+
                    "<p>Here is your verify code for set new password"+""+
                    "Please, verify your account with the code  below to complete your reset Password.</p>"+
                    "<div> <h2>" + code + "</h2></div>\n" +
                    "<p> Thank you "+
                    "</body>\n" +
                    "</html>\n";

            MimeMessage message = javaMailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper (message) ;
            messageHelper.setFrom( "sunsythorng9999@gmail.com", senderName);
            messageHelper.setTo(userApp.getEmail());
            messageHelper.setSubject(subject);
            messageHelper.setText(html,true) ;

            javaMailSender.send (message) ;
            return "Mail Successfully...";
        } catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

}

