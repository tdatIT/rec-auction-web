package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.AuctSessJoin;
import com.ec.recauctionec.data.entities.AuctionSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AuctSessJoinRepo extends JpaRepository<AuctSessJoin, Long> {
    @Query("select j from AuctSessJoin j where j.auctionSession.auctionSessId = ?1")
    List<AuctSessJoin> findAllByAuctionId(int auctionId);


    AuctSessJoin findFirstByAuctionSessionOrderByPriceAsc(AuctionSession auctionSession);

    @Query("select j from AuctSessJoin j " +
            "where j.product.supplier.supplierId = ?1 " +
            "and j.auctionSession.endDate>=?2")
    List<AuctSessJoin> findBySupplierAndDate(int supplierId, Date date);

    @Query("select count(u) from AuctSessJoin u where u.product.supplier.supplierId =?1")
    long totalBidJoinOfSupplier(int supplierId);

    @Query("select count(u) from AuctSessJoin u where u.product.supplier.supplierId = ?1 " +
            "and u.status = 2")
    long totalBidActiveOfSupplier(int supplier);
}
