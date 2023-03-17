package com.ec.recauctionec.services;

import com.ec.recauctionec.dto.OrderDTO;
import com.ec.recauctionec.entities.Orders;
import com.ec.recauctionec.entities.Supplier;

import java.util.Date;
import java.util.List;

public interface OrderService {
    Orders findById(int id);

    List<Orders> findOrderNonConfirm();

    List<Orders> findOrderByDate(int userId, Date date);

    List<Orders> findAll();

    List<Orders> findAllBySupplierDate(Supplier supplier, Date filterDate);
    long numberOfOrdersPendingOfSupplier(Supplier supplier,Date filterDate);
    long numberOfOrdersOfSupplier(Supplier supplier,Date filterDate);

    void createOrderNotConfirm(OrderDTO dto);

    boolean confirmOrder(OrderDTO dto);

    boolean cancelOrder(OrderDTO dto);

    boolean completedOrder(Orders order);

    boolean deliveryOrder(OrderDTO dto);

    void updateOrder(Orders orders);

}
