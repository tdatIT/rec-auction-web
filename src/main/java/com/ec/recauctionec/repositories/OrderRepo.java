package com.ec.recauctionec.repositories;

import com.ec.recauctionec.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface OrderRepo extends JpaRepository<Orders, Integer> {
    @Query("select o from Orders o " +
            "where o.status = 1 " +
            "and o.createDate >= :check")
        //NOT_CONFIRM = 1
    List<Orders> findByNonConfirm(@Param("check") Timestamp check);

    @Query("select o from  Orders o where date(o.createDate)=?2 and o.user.userId =?1")
    List<Orders> findOrderByDate(int userId, Date date);

    @Query("select o from Orders o where o.orderId = ?1")
    Orders findByOrderId(int orderId);
}
