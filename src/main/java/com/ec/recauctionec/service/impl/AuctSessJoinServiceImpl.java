package com.ec.recauctionec.service.impl;

import com.ec.recauctionec.entities.AuctSessJoin;
import com.ec.recauctionec.repositories.AuctSessJoinRepo;
import com.ec.recauctionec.service.AuctSessJoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctSessJoinServiceImpl implements AuctSessJoinService {
    @Autowired
    private AuctSessJoinRepo joinRepo;
    @Override
    public List<AuctSessJoin> findAllByAuctionId(int auctionId) {
        return joinRepo.findAllByAuctionId(auctionId);
    }

    @Override
    public AuctSessJoin findBestPriceAuctionJoinByAuction(int auctionId) {
        return null;
    }

    @Override
    public boolean joinAuction(AuctSessJoin join) {
        joinRepo.save(join);
        return true;
    }

    @Override
    public boolean updateJoin(AuctSessJoin join) {
        joinRepo.save(join);
        return true;
    }

    @Override
    public AuctSessJoin findById(long id) {
        return joinRepo.findById(id).orElseThrow();
    }
}
