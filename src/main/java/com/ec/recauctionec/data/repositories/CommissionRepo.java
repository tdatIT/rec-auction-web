package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.Commission;
import com.ec.recauctionec.data.response.CommissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommissionRepo extends JpaRepository<Commission, Integer> {

    @Query("select sum(c.amountFromBuyer) as fromBuyer," +
            "sum(c.amountFromSupplier) as fromSupplier, " +
            "date(o.createdDate) as createDate " +
            "from Commission c " +
            "inner join Orders o on c.orderId = o.orderId " +
            "where month(o.createdDate)=?1 and year (o.createdDate)=?2 " +
            "group by o.createdDate")
    List<CommissionType> getTotalCommissionInMonth(Integer month, Integer year);
}
