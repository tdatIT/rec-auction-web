package com.ec.recauctionec.service.impl;

import com.ec.recauctionec.email.EmailDetails;
import com.ec.recauctionec.entities.User;
import com.ec.recauctionec.service.EmailService;
import com.ec.recauctionec.variable.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public boolean sendSimpleEmail(EmailDetails details) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(details.getRecipient());
            simpleMailMessage.setSubject(details.getSubject());
            simpleMailMessage.setText(details.getMsgBody());
            //Send email
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean sendResetPassword(User us, String token) {
        try {
            String recipientAddress = us.getEmail();
            String subject = "Xác nhận đăng ký tài khoản";
            String confirmationUrl
                    = "/confirm-reset?token=" + token;
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(recipientAddress);
            email.setSubject(subject);
            email.setText("Nhấn vào link bên dưới để xác thực tài khoản" + "\r\n" + PathVariable.CONTEXT_PATH + confirmationUrl);
            javaMailSender.send(email);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
