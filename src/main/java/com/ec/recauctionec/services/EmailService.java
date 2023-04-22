package com.ec.recauctionec.services;

import com.ec.recauctionec.data.entities.Bid;
import com.ec.recauctionec.data.entities.Orders;
import com.ec.recauctionec.data.entities.User;

public interface EmailService {
    boolean sendVerifyEmail(User us, String token, String requestUrl);

    boolean sendNotifyEmail(String email, Bid bid);

    boolean sendMailOrder(Orders orders);
}
