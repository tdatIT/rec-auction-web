package com.ec.recauctionec.scheduling;

import com.ec.recauctionec.dto.OrderDTO;
import com.ec.recauctionec.email.EmailDetails;
import com.ec.recauctionec.entities.AuctSessJoin;
import com.ec.recauctionec.entities.AuctionSession;
import com.ec.recauctionec.entities.Orders;
import com.ec.recauctionec.entities.User;
import com.ec.recauctionec.service.AuctionService;
import com.ec.recauctionec.service.EmailService;
import com.ec.recauctionec.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Component
public class CheckAuctionScheduledEnd {
    private static final Logger log =
            LoggerFactory.getLogger(CheckAuctionScheduledEnd.class);
    private static final int MIN_SCHEDULED = 10;
    private static final int MIN_NOTIFY = 30;
    private static final int MILLISECOND = 60000;
    private static Calendar calendar;

    @Autowired
    private AuctionService auctionService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OrderService orderService;

    @Scheduled(fixedRate = MIN_SCHEDULED * MILLISECOND)
    public void checkEndTimeAuction() {
        log.info("---Scheduled check end time auctions run---");
        calendar = Calendar.getInstance();
        List<AuctionSession> auctions = auctionService
                .findAllByDate(new Date(new java.util.Date().getTime()));
        for (AuctionSession auction : auctions) {
            if (auction.getEndDate().getTime() <= calendar.getTimeInMillis()) {
                AuctSessJoin win = auctionService.setWinAuctionSession(auction.getAuctionSessId());
                OrderDTO dto = new OrderDTO();
                User us = auction.getUser();
                dto.setUser(us);
                //set default address
                dto.setProduct(win.getProduct());
                dto.setTotalPrice(win.getPrice());
                dto.setStatus(Orders.NOT_CONFIRM);
                dto.setWinAuction(win);
                dto.setCreateDate(new Timestamp(new java.util.Date().getTime()));
                orderService.createOrderNotConfirm(dto);
                log.info("Create order of Auction ID:  " + auction.getAuctionSessId());
            } else if (auction.getEndDate().getTime() <= calendar.getTimeInMillis() + (MIN_NOTIFY * MILLISECOND)) {
                EmailDetails email = new EmailDetails();
                email.setRecipient(auction.getUser().getEmail());
                email.setSubject("Phiên Đấu Giá Sắp Kết Thúc");
                email.setMsgBody("Phiên đấu giá ID:[" + auction.getAuctionSessId() +
                        "] của bạn còn 30p nữa là hết hạn]");
                emailService.sendSimpleEmail(email);
                log.info("Send mail notify will end auction to: " + email.getRecipient());
            }
        }

    }

    @Scheduled(fixedRate = MIN_NOTIFY * MILLISECOND)
    public void checkConfirmOrder() {
        log.info("---Scheduled check end time orders confirm---");
        calendar = Calendar.getInstance();
        List<Orders> orders = orderService.findOrderNonConfirm();
        for (Orders o : orders) {
            OrderDTO dto = new OrderDTO();
            BeanUtils.copyProperties(o, dto);
            orderService.cancelOrder(dto);
            log.info("Cancel Order ID: " + dto.getOrderId());
        }

    }

}
