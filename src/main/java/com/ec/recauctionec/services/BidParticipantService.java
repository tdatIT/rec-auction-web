package com.ec.recauctionec.services;

import com.ec.recauctionec.data.entities.BidParticipant;
import com.ec.recauctionec.data.entities.Bid;
import com.ec.recauctionec.data.entities.Supplier;

import java.sql.Date;
import java.util.List;

public interface BidParticipantService {
    List<BidParticipant> findAllByAuctionId(int auctionId);

    BidParticipant findBestPriceAuctionJoinByAuction(Bid bid);

    boolean joinAuction(BidParticipant join);

    boolean updateJoin(BidParticipant join);

    BidParticipant findById(long id);

    List<BidParticipant> findAllBySupplierAndDate(Supplier suppliers, Date date);

    long totalBidJoinOfSupplier(Supplier supplier);
    long totalBidActiveOfSupplier(Supplier supplier);
}
