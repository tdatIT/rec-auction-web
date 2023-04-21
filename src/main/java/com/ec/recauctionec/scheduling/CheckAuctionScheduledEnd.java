package com.ec.recauctionec.scheduling;

import com.ec.recauctionec.data.dto.OrderDTO;
import com.ec.recauctionec.data.entities.Bid;
import com.ec.recauctionec.data.entities.BidJoin;
import com.ec.recauctionec.data.entities.Orders;
import com.ec.recauctionec.data.entities.User;
import com.ec.recauctionec.services.BidJoinService;
import com.ec.recauctionec.services.BidService;
import com.ec.recauctionec.services.EmailService;
import com.ec.recauctionec.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private BidService bidService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BidJoinService joinService;

    @Transactional
    @Scheduled(fixedRate = MIN_SCHEDULED * MILLISECOND)
    public void checkEndTimeAuction() {
        log.info("---Scheduled check end time bid has been started---");
        calendar = Calendar.getInstance();
        List<Bid> bids = bidService
                .findAllByDate(new Date(new java.util.Date().getTime()));
        for (Bid bid : bids) {
            if (bid.getEndDate().getTime() <= calendar.getTimeInMillis()) {
                bidService.setWinAuctionSession(bid.getBidId());
                BidJoin win = joinService.findBestPriceAuctionJoinByAuction(bid);
                if (win != null) {
                    OrderDTO dto = new OrderDTO();
                    User us = bid.getUser();
                    dto.setUser(us);
                    //set default address
                    dto.setProduct(win.getProduct());
                    dto.setTotalPrice(win.getPrice());
                    dto.setStatus(Orders.NOT_CONFIRM);

                    dto.setCreateDate(new Timestamp(new java.util.Date().getTime()));
                    orderService.createOrderNotConfirm(dto);
                    log.info("Create order of Auction ID:  " + bid.getBidId());
                }
            } else if (bid.getEndDate().getTime() <= calendar.getTimeInMillis() + (MIN_NOTIFY * MILLISECOND)) {
                User us = bid.getUser();
                emailService.sendNotifyEmail(us.getEmail(), bid);
                log.info("Send mail notify will end auction to: " + us.getEmail());
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
