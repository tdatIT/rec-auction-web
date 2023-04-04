package com.ec.recauctionec.services;

import com.ec.recauctionec.data.entities.BidJoin;
import com.ec.recauctionec.data.entities.Bid;
import com.ec.recauctionec.data.entities.Supplier;

import java.sql.Date;
import java.util.List;

public interface BidJoinService {
    List<BidJoin> findAllByAuctionId(int auctionId);

    BidJoin findBestPriceAuctionJoinByAuction(Bid bid);

    boolean joinAuction(BidJoin join);

    boolean updateJoin(BidJoin join);

    BidJoin findById(long id);

    List<BidJoin> findAllBySupplierAndDate(Supplier suppliers, Date date);

    long totalBidJoinOfSupplier(Supplier supplier);
    long totalBidActiveOfSupplier(Supplier supplier);
}
