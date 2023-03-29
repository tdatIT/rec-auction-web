package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.Orders;
import org.springframework.data.domain.Pageable;
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

    @Query("select o from Orders o where o.product.supplier.supplierId =?1 " +
            "and date(o.createDate)>=?2 and o.status != 1")
    List<Orders> findAllBySupplierAndDate(int supplierId, Date filterDate);

    @Query("select count(o) from Orders o where (o.product.supplier.supplierId =?1 " +
            "and date(o.createDate)>=?2) and o.status = 2")
    long totalPendingOrderByDate(int supplierId, Date date);

    @Query("select count(o) from Orders o where o.product.supplier.supplierId =?1 " +
            "and date(o.createDate)>=?2")
    long totalOrderByDate(int supplierId, Date date);

    @Query("select count(o) from Orders o where o.createDate >= ?1")
    long totalOrderFromDateForAdmin(java.sql.Date filter);
    @Query("select o from Orders o where o.createDate >= ?1 order by o.createDate desc")
    List<Orders> findAllOrdersByDate(java.sql.Date filter, Pageable pageable);
}
