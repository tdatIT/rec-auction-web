package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.BidJoin;
import com.ec.recauctionec.data.entities.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BidJoinRepos extends JpaRepository<BidJoin, Long> {
    @Query("select j from BidJoin j where j.bid.bidId = ?1")
    List<BidJoin> findAllByAuctionId(int auctionId);


    BidJoin findFirstByBidOrderByPriceAsc(Bid bid);

    @Query("select j from BidJoin j " +
            "where j.product.supplier.supplierId = ?1 " +
            "and j.bid.endDate>=?2")
    List<BidJoin> findBySupplierAndDate(int supplierId, Date date);

    @Query("select count(u) from BidJoin u where u.product.supplier.supplierId =?1")
    long totalBidJoinOfSupplier(int supplierId);

    @Query("select count(u) from BidJoin u where u.product.supplier.supplierId = ?1 " +
            "and u.status = 2")
    long totalBidActiveOfSupplier(int supplier);
}
