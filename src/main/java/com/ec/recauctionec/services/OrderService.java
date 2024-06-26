package com.ec.recauctionec.services;

import com.ec.recauctionec.data.dto.OrderDTO;
import com.ec.recauctionec.data.entities.Orders;
import com.ec.recauctionec.data.entities.Supplier;
import com.ec.recauctionec.data.entities.User;
import com.ec.recauctionec.data.response.OrderTypeQuery;

import java.util.Date;
import java.util.List;

public interface OrderService {
    Orders findById(int id);
    List<Orders> findOrderNonConfirm();
    List<Orders> findOrderByDate(int userId, Date date);
    List<Orders> findAll();
    List<Orders> find5LastOrderBySupplier(Supplier supplier);
    List<Orders> findAllBySupplierDate(Supplier supplier, Date filterDate);
    List<Orders> find5LastOrder(User user);
    long numberOfOrdersPendingOfSupplier(Supplier supplier, Date filterDate);
    long numberOfOrdersOfSupplier(Supplier supplier, Date filterDate);
    void createOrderNotConfirm(OrderDTO dto);
    boolean confirmOrder(OrderDTO dto);
    boolean cancelOrder(int orderId);
    boolean completedOrder(OrderDTO dto);
    boolean deliveryOrder(OrderDTO dto);
    void updateOrder(Orders orders);
    long totalOrderFromDate(java.sql.Date filter);
    List<Orders> findAllOrderByDate(java.sql.Date filter, Integer page, Integer size);
    List<OrderTypeQuery> getTotalOrderInMonth(Integer month, Integer year);
}
