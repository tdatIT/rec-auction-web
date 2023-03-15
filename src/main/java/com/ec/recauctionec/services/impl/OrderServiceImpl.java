package com.ec.recauctionec.services.impl;

import com.ec.recauctionec.dto.OrderDTO;
import com.ec.recauctionec.entities.*;
import com.ec.recauctionec.location.Shipping;
import com.ec.recauctionec.repositories.*;
import com.ec.recauctionec.services.AuctSessJoinService;
import com.ec.recauctionec.services.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ModelMapper modelMapper;
    private static final Logger log =
            LoggerFactory.getLogger(OrderServiceImpl.class);
    //Percent for 1 transaction for E-Commerce Exchange
    private static final double FROM_SUPPLIER = 0.05;

    private static final double FROM_BUYER = 0.25;
    //Viettel POST
    private static final int DEFAULT_SHIPPING = 1;
    //status order

    @Autowired
    private CommissionRepo commissionRepo;

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private DeliveryRepo deliveryRepo;
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private WalletHistoryRepo historyRepo;
    @Autowired
    private AuctSessJoinService joinService;

    @Override
    public Orders findById(int id) {
        return orderRepo.findByOrderId(id);
    }

    @Override
    public List<Orders> findOrderNonConfirm() {
        Calendar current = Calendar.getInstance();
        //Add 1 day
        current.add(Calendar.DAY_OF_MONTH, 1);
        Timestamp checkTime = new Timestamp(current.getTimeInMillis());
        return orderRepo.findByNonConfirm(checkTime);
    }

    @Override
    @Transactional
    public void createOrderNotConfirm(OrderDTO dto) {
        Orders order = modelMapper.map(dto, Orders.class);
        order.setDelivery(new Delivery(DEFAULT_SHIPPING));
        order.setStatus(Orders.NOT_CONFIRM);
        order.setCreateDate(new Timestamp(new Date().getTime()));
        orderRepo.save(order);
    }

    @Override
    @Transactional
    public boolean confirmOrder(OrderDTO dto) {
        try {
            Orders order = dto.mapping();
            Wallet user_wallet = dto.getUser().getWallet();
            if (user_wallet.getAccountBalance() >= order.getTotalPrice()) {
                //Calculate Shipping Cost
                AddressData src = order.getProduct().getSupplier().getAddresses().get(0);
                AddressData des = order.getAddress();
                order.setShippingPrice(Shipping.calculateShipping(src, des,
                        deliveryRepo.findById(DEFAULT_SHIPPING).orElseThrow()));
                //Calculate commission of transaction
                //Set more info of order
                order.setStatus(Orders.CONFIRM);
                order.setUpdateDate(new java.sql.Date(new Date().getTime()));
                orderRepo.save(order);
                //Create log in wallet of user
                WalletHistory log1 = new WalletHistory();
                log1.setType(false);
                log1.setValue(order.getTotalPrice());
                log1.setWallet(user_wallet);
                log1.setCreateDate(new Timestamp(new Date().getTime()));
                log1.setPaymentId("CHARGE ORDER");
                //Charge into wallet
                user_wallet.setAccountBalance(
                        user_wallet.getAccountBalance() - log1.getValue());
                walletRepo.save(user_wallet);
                historyRepo.save(log1);
                return true;
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return false;
    }

    @Override
    @Transactional
    public boolean cancelOrder(OrderDTO dto) {
        try {
            Orders order = dto.mapping();
            Wallet user_wallet = dto.getUser().getWallet();
            if (order.getStatus() != Orders.CANCEL && order.getStatus() != Orders.COMPLETE) {
                order.setStatus(Orders.CANCEL);
                order.setUpdateDate(new java.sql.Date(new Date().getTime()));
                //Create log in wallet of user by status
                WalletHistory log1 = new WalletHistory();
                if (order.getStatus() != Orders.NOT_CONFIRM) {
                    log1.setType(true);
                    log1.setValue(order.getTotalPrice());
                    log1.setWallet(user_wallet);
                    log1.setPaymentId("ROLL_BACK_ORDER");
                    //Charge into wallet
                    user_wallet.setAccountBalance(
                            user_wallet.getAccountBalance() + order.getTotalPrice() * 0.8);
                } else {
                    if (user_wallet.getAccountBalance() >=
                            order.getTotalPrice() * 0.3) {
                        log1.setType(false);
                        log1.setValue(order.getTotalPrice());
                        log1.setWallet(user_wallet);
                        log1.setPaymentId("CANCEL_ORDER_CHARGE");
                        //Charge into wallet
                        user_wallet.setAccountBalance(
                                user_wallet.getAccountBalance() - order.getTotalPrice() * 0.3
                        );
                    }
                    return false;
                }
                historyRepo.save(log1);
                walletRepo.save(user_wallet);
                return true;
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return false;
    }

    @Override
    @Transactional
    public boolean completedOrder(Orders order) {
        try {
            Wallet user_wallet = order.getProduct()
                    .getSupplier()
                    .getUser()
                    .getWallet();
            if (order.getStatus() == Orders.DELIVERY) {
                order.setStatus(Orders.COMPLETE);
                order.setUpdateDate(new java.sql.Date(new Date().getTime()));
                //Create commission
                Commission commission = new Commission();
                double realValue = order.getTotalPrice() - order.getShippingPrice();
                commission.setAmountFromSupplier(realValue * FROM_SUPPLIER);
                /*  double profit = order.getWinAuction()
                        .getAuctionSession()
                        .getReservePrice() - order.getTotalPrice();*//*
                commission.setAmountFromBuyer(profit * FROM_BUYER);*/
                commission.setOrder(order);
                //Create log in wallet of user by status
                WalletHistory log1 = new WalletHistory();
                log1.setType(true);
                log1.setValue(order.getTotalPrice() - commission.getAmountFromSupplier());
                log1.setWallet(user_wallet);
                log1.setPaymentId("ROLL_BACK_ORDER");
                log1.setCreateDate(new Timestamp(new Date().getTime()));
                //transfer money into wallet
                user_wallet.setAccountBalance(
                        user_wallet.getAccountBalance() + log1.getValue());
                historyRepo.save(log1);
                walletRepo.save(user_wallet);
                commissionRepo.save(commission);
                return true;
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deliveryOrder(OrderDTO dto) {
        Orders order = dto.mapping();
        order.setStatus(Orders.DELIVERY);
        order.setUpdateDate(new java.sql.Date(new Date().getTime()));
        orderRepo.save(order);
        return true;
    }

    @Override
    public List<Orders> findOrderByDate(int userId, Date date) {
        return orderRepo.findOrderByDate(userId, date);
    }

    @Override
    public List<Orders> findAll() {
        return orderRepo.findAll();
    }

    @Override
    public void updateOrder(Orders orders) {
        orderRepo.save(orders);
    }

}
