package com.ec.recauctionec.repositories;

import com.ec.recauctionec.entities.AuctSessJoin;
import com.ec.recauctionec.entities.AuctionSession;
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
            "and j.auctionSession.endDate=?2")
    List<AuctSessJoin> findBySupplierAndDate(long supplierId, Date date);



}
