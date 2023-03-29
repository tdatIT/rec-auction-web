package com.ec.recauctionec.services;

import com.ec.recauctionec.data.email.EmailDetails;
import com.ec.recauctionec.data.entities.User;

public interface EmailService {
    boolean sendSimpleEmail(EmailDetails details);
    boolean sendResetPassword(User us,String token);
}
