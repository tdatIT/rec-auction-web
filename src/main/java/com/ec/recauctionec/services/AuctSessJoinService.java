package com.ec.recauctionec.services;

import com.ec.recauctionec.data.entities.AuctSessJoin;
import com.ec.recauctionec.data.entities.AuctionSession;
import com.ec.recauctionec.data.entities.Supplier;

import java.sql.Date;
import java.util.List;

public interface AuctSessJoinService {
    List<AuctSessJoin> findAllByAuctionId(int auctionId);

    AuctSessJoin findBestPriceAuctionJoinByAuction(AuctionSession auctionSession);

    boolean joinAuction(AuctSessJoin join);

    boolean updateJoin(AuctSessJoin join);

    AuctSessJoin findById(long id);

    List<AuctSessJoin> findAllBySupplierAndDate(Supplier suppliers, Date date);

    long totalBidJoinOfSupplier(Supplier supplier);
    long totalBidActiveOfSupplier(Supplier supplier);
}
