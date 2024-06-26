package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.Orders;
import com.ec.recauctionec.data.response.OrderTypeQuery;
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
            "and o.createdDate >= :check")
        //NOT_CONFIRM = 1
    List<Orders> findByNonConfirm(@Param("check") Timestamp check);

    @Query("select o from  Orders o where date(o.createdDate)=?2 and o.user.userId =?1")
    List<Orders> findOrderByDate(int userId, Date date);

    @Query("select o from Orders o where o.orderId = ?1")
    Orders findByOrderId(int orderId);

    @Query("select o from Orders o where o.product.supplier.supplierId =?1 " +
            "and date(o.createdDate)>=?2 and o.status != 1")
    List<Orders> findAllBySupplierAndDate(int supplierId, Date filterDate);

    @Query("select o from Orders o where o.product.supplier.supplierId =?1 " +
            "and o.status != 1")
    List<Orders> find5LastBySupplier(int supplierId, Pageable pageable);


    @Query("select count(o) from Orders o where (o.product.supplier.supplierId =?1 " +
            "and date(o.createdDate)>=?2) and o.status = 2")
    long totalPendingOrderByDate(int supplierId, Date date);

    @Query("select count(o) from Orders o where o.product.supplier.supplierId =?1 " +
            "and date(o.createdDate)>=?2")
    long totalOrderByDate(int supplierId, Date date);

    @Query("select count(o) from Orders o where o.createdDate >= ?1")
    long totalOrderFromDateForAdmin(java.sql.Date filter);

    @Query("select o from Orders o where o.createdDate >= ?1 order by o.createdDate desc")
    List<Orders> findAllOrdersByDate(java.sql.Date filter, Pageable pageable);

    @Query("select o from Orders o where  o.user.userId=?1 order by o.createdDate desc ")
    List<Orders> find5LastOrders(int userId, Pageable pageable);

    @Query("select o.status as type, count(o.status) as total from Orders o" +
            " where month(o.createdDate)=?1 and year(o.createdDate)=?2" +
            " group by o.status")
    List<OrderTypeQuery> totalOrderInMonthAndGroupByStatus(Integer month, Integer year);

}
