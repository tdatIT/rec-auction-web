package com.ec.recauctionec.event;

import com.ec.recauctionec.entities.User;
import com.ec.recauctionec.service.UserService;
import com.ec.recauctionec.variable.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    @Autowired
    private UserService service;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Xác nhận đăng ký tài khoản";
        String confirmationUrl
                = event.getAppUrl() + "/registration-confirm?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Nhấn vào link bên dưới để xác thực tài khoản" + "\r\n" + PathVariable.CONTEXT_PATH + confirmationUrl);
        mailSender.send(email);
    }
}
