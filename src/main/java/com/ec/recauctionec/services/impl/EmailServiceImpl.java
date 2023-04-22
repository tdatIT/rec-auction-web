package com.ec.recauctionec.services.impl;

import com.ec.recauctionec.data.email.EmailDetails;
import com.ec.recauctionec.data.email.EmailFactory;
import com.ec.recauctionec.data.entities.Bid;
import com.ec.recauctionec.data.entities.Orders;
import com.ec.recauctionec.data.entities.User;
import com.ec.recauctionec.data.variable.PathVariable;
import com.ec.recauctionec.services.EmailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Log4j2
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    private void sendMail(EmailDetails details) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("noreply@recaution.com");
        helper.setTo(details.getRecipient());
        helper.setSubject(details.getSubject());
        helper.setText(details.getMsgBody(), true);
        javaMailSender.send(message);
        log.info("Send email verify account [" + details.getRecipient() + "]");
    }

    @Override
    public boolean sendVerifyEmail(User us, String token, String requestUrl) {
        try {
            String link = PathVariable.CONTEXT_PATH + requestUrl + "?token=" + token;
            EmailDetails email = EmailFactory.getVerifyEmail(link, us.getEmail());
            sendMail(email);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean sendNotifyEmail(String email, Bid bid) {
        try {
            String link = PathVariable.CONTEXT_PATH + "/chi-tiet-dau-gia/" + bid.getBidId();
            EmailDetails email_detail = EmailFactory.getNotifyEmail(link, email);
            sendMail(email_detail);
            log.info("Send email verify account [" + email + "]");
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean sendMailOrder(Orders orders) {
        try {
            EmailDetails email_detail = EmailFactory.getOrderEmail(orders);
            sendMail(email_detail);
            log.info("Send mail order confirm success has successful");
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
