package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.BidParticipant;
import com.ec.recauctionec.data.entities.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BidParticipantRepos extends JpaRepository<BidParticipant, Long> {
    @Query("select j from BidParticipant j where j.bid.bidId = ?1")
    List<BidParticipant> findAllByAuctionId(int auctionId);


    BidParticipant findFirstByBidOrderByPriceAsc(Bid bid);

    @Query("select j from BidParticipant j " +
            "where j.product.supplier.supplierId = ?1 " +
            "and j.bid.endedDate>=?2")
    List<BidParticipant> findBySupplierAndDate(int supplierId, Date date);

    @Query("select count(u) from BidParticipant u where u.product.supplier.supplierId =?1")
    long totalBidJoinOfSupplier(int supplierId);

    @Query("select count(u) from BidParticipant u where u.product.supplier.supplierId = ?1 " +
            "and u.status = 2")
    long totalBidActiveOfSupplier(int supplier);
}
