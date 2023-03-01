package com.ec.recauctionec.service;

import com.ec.recauctionec.entities.AuctSessJoin;

import java.util.List;

public interface AuctSessJoinService {
    List<AuctSessJoin> findAllByAuctionId(int auctionId);

    AuctSessJoin findBestPriceAuctionJoinByAuction(int auctionId);

    boolean joinAuction(AuctSessJoin join);

    boolean updateJoin(AuctSessJoin join);

    AuctSessJoin findById(long id);
}
