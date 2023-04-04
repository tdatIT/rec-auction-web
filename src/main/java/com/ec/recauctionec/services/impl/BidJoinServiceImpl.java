package com.ec.recauctionec.services.impl;

import com.ec.recauctionec.data.entities.BidJoin;
import com.ec.recauctionec.data.entities.Bid;
import com.ec.recauctionec.data.entities.Supplier;
import com.ec.recauctionec.data.repositories.BidJoinRepos;
import com.ec.recauctionec.services.BidJoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class BidJoinServiceImpl implements BidJoinService {
    @Autowired
    private BidJoinRepos joinRepo;

    @Override
    public List<BidJoin> findAllByAuctionId(int auctionId) {
        return joinRepo.findAllByAuctionId(auctionId);
    }

    @Override
    public BidJoin findBestPriceAuctionJoinByAuction(Bid bid) {
        return joinRepo.findFirstByBidOrderByPriceAsc(bid);
    }

    @Override
    public boolean joinAuction(BidJoin join) {
        joinRepo.save(join);
        return true;
    }

    @Override
    public boolean updateJoin(BidJoin join) {
        joinRepo.save(join);
        return true;
    }

    @Override
    public BidJoin findById(long id) {
        return joinRepo.findById(id).orElseThrow();
    }

    @Override
    public List<BidJoin> findAllBySupplierAndDate(Supplier suppliers, Date date) {
        return joinRepo.findBySupplierAndDate(suppliers.getSupplierId(), date);
    }

    @Override
    public long totalBidJoinOfSupplier(Supplier supplier) {
        return joinRepo.totalBidJoinOfSupplier(supplier.getSupplierId());
    }

    @Override
    public long totalBidActiveOfSupplier(Supplier supplier) {
        return joinRepo.totalBidActiveOfSupplier(supplier.getSupplierId());
    }
}
