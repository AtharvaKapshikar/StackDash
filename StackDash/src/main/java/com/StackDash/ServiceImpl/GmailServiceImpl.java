package com.StackDash.ServiceImpl;

import com.StackDash.Entity.User;
import com.StackDash.Service.GmailService;
import com.StackDash.Service.OtpService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.logging.Logger;

@Service
public class GmailServiceImpl implements GmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendOtpToUser(User user, String otp) {
        try {
            // mail body to send

            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("atharvakapshikar2002@gmail.com");

            if (user.getEmail() != null && !user.getEmail().isEmpty() && otp != null && !otp.isEmpty()) {
                message.setTo(user.getEmail());
            }
            message.setSubject("Verify Your Email");
            message.setText("Hi " + user.getFirstName() + ",\n\n"
                    + "Thanks for registering with StackDash!\n"
                    + "Your OTP is: " + otp + "\n\n"
                    + "This OTP is valid for 5 minutes.\n"
                    + "Please do not share it with anyone.\n\n"
                    + "Cheers,\n"
                    + "The StackDash Team");

            //mail sending
            mailSender.send(message);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    //method to send otp to user
}
