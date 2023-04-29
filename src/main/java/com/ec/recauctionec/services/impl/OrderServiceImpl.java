package com.ec.recauctionec.services.impl;

import com.ec.recauctionec.data.dto.OrderDTO;
import com.ec.recauctionec.data.entities.*;
import com.ec.recauctionec.data.repositories.*;
import com.ec.recauctionec.data.response.OrderTypeQuery;
import com.ec.recauctionec.services.BidJoinService;
import com.ec.recauctionec.services.EmailService;
import com.ec.recauctionec.services.OrderService;
import com.ec.recauctionec.services.shipping.Shipping;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    private static final double FROM_BUYER = 0.15;
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
    private BidJoinService joinService;
    @Autowired
    private EmailService emailService;

    @Override
    public Orders findById(int id) {
        return orderRepo.findByOrderId(id);
    }

    @Override
    public long totalOrderFromDate(java.sql.Date filter) {
        return orderRepo.totalOrderFromDateForAdmin(filter);
    }

    @Override
    public List<Orders> findAllOrderByDate(java.sql.Date filter, Integer page, Integer size) {
        return orderRepo.findAllOrdersByDate(filter, PageRequest.of(page, size));
    }

    @Override
    public List<Orders> find5LastOrderBySupplier(Supplier supplier) {
        return orderRepo.find5LastBySupplier(supplier.getSupplierId(), PageRequest.of(0, 5));
    }

    @Override
    public List<Orders> find5LastOrder(User user) {
        return orderRepo.find5LastOrders(user.getUserId(), PageRequest.of(0, 5));
    }

    @Override
    public List<Orders> findAllBySupplierDate(Supplier supplier, Date filterDate) {
        return orderRepo.findAllBySupplierAndDate(supplier.getSupplierId(), filterDate);
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
    public long numberOfOrdersPendingOfSupplier(Supplier supplier, Date filterDate) {
        return orderRepo.totalPendingOrderByDate(supplier.getSupplierId(), filterDate);
    }

    @Override
    public long numberOfOrdersOfSupplier(Supplier supplier, Date filterDate) {
        return orderRepo.totalOrderByDate(supplier.getSupplierId(), filterDate);
    }

    @Override
    public List<OrderTypeQuery> getTotalOrderInMonth(Integer month, Integer year) {
        return orderRepo.totalOrderInMonthAndGroupByStatus(month, year);
    }

    @Override
    @Transactional
    public void createOrderNotConfirm(OrderDTO dto) {
        Orders order = modelMapper.map(dto, Orders.class);
        order.setDelivery(new Delivery(DEFAULT_SHIPPING));
        order.setStatus(Orders.NOT_CONFIRM);
        order.setCreateDate(new Timestamp(new Date().getTime()));
        order.setUpdateDate(new java.sql.Date(new Date().getTime()));
        orderRepo.save(order);
    }

    @Override
    @Transactional
    public boolean confirmOrder(OrderDTO dto) {
        try {
            Delivery default_deli = deliveryRepo.findById(DEFAULT_SHIPPING).orElseThrow();
            Orders order = dto.mapping();
            Wallet user_wallet = dto.getUser().getWallet();
            if (user_wallet.getAccountBalance() >= order.getTotalPrice()) {
                //Calculate Shipping Cost
                AddressData src = order.getProduct().getSupplier().getAddresses().get(0);
                AddressData des = order.getAddress();
                order.setShippingPrice(Shipping.calculateShipping(src, des, default_deli));
                order.setDelivery(default_deli);
                //set new total price = old_total + shipping
                order.setTotalPrice(dto.getTotalPrice() + order.getShippingPrice());
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
                emailService.sendMailOrder(order);
                return true;
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return false;
    }

    @Override
    @Transactional
    public boolean cancelOrder(int orderId) {
        try {
            Orders order = orderRepo.findByOrderId(orderId);
            Wallet user_wallet = order.getUser().getWallet();
            //Create a log of user wallet transactions
            WalletHistory log1 = new WalletHistory();
            if (order.getStatus() != Orders.CANCEL && order.getStatus() != Orders.COMPLETE) {
                //If the order was canceled by the supplier when the order has been confirmed and shipped,
                //then the money will be rolled back to the user's wallet
                if (order.getStatus() == Orders.CONFIRM
                        && order.getStatus() == Orders.DELIVERY) {
                    log1.setType(true);
                    log1.setValue(order.getTotalPrice());
                    log1.setWallet(user_wallet);
                    log1.setPaymentId("RB_ORDER");
                    log1.setCreateDate(new Timestamp(new Date().getTime()));
                    //Charge into wallet
                    user_wallet.setAccountBalance(
                            user_wallet.getAccountBalance() + order.getTotalPrice() * 0.1);
                }
                //Money will be charged when an order created by the system is not confirmed by the user.
                if (order.getStatus() == Orders.NOT_CONFIRM &&
                        user_wallet.getAccountBalance() >= order.getTotalPrice() * 0.3) {
                    log1.setType(false);
                    log1.setValue(order.getTotalPrice());
                    log1.setCreateDate(new Timestamp(new Date().getTime()));
                    log1.setWallet(user_wallet);
                    log1.setPaymentId("CHARGE_CO");
                    //Charge money
                    user_wallet.setAccountBalance(
                            user_wallet.getAccountBalance() - order.getTotalPrice() * 0.3
                    );
                }
            }
            order.setUpdateDate(new java.sql.Date(new Date().getTime()));
            order.setStatus(Orders.CANCEL);
            orderRepo.save(order);
            historyRepo.save(log1);
            walletRepo.save(user_wallet);
            return true;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return false;
    }

    @Override
    @Transactional
    public boolean completedOrder(OrderDTO dto) {
        Orders order = orderRepo.findByOrderId(dto.getOrderId());
        Wallet user_wallet = order.getProduct()
                .getSupplier()
                .getUser()
                .getWallet();
        if (order.getStatus() == Orders.DELIVERY) {
            order.setStatus(Orders.COMPLETE);
            order.setUpdateDate(new java.sql.Date(new Date().getTime()));
            //Create commission
            Commission commission = new Commission();
            double realValue = dto.getTotalPrice() - dto.getShippingPrice();
            commission.setAmountFromSupplier(realValue * FROM_SUPPLIER);
            double profit = dto.getShippingPrice();
            commission.setAmountFromBuyer(profit);
            commission.setOrder(order);
            //Create log in wallet of user by status
            WalletHistory log1 = new WalletHistory();
            log1.setType(true);
            log1.setValue(dto.getTotalPrice() - commission.getAmountFromSupplier());
            log1.setWallet(user_wallet);
            log1.setPaymentId("ROLL_BACK_ORDER");
            log1.setCreateDate(new Timestamp(new Date().getTime()));
            //transfer money into wallet
            user_wallet.setAccountBalance(
                    user_wallet.getAccountBalance() + log1.getValue());
            //save change into database
            orderRepo.save(order);
            historyRepo.save(log1);
            walletRepo.save(user_wallet);
            commissionRepo.save(commission);
            return true;
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
