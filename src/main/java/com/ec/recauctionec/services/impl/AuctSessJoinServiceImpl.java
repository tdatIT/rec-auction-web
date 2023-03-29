package com.ec.recauctionec.services.impl;

import com.ec.recauctionec.data.entities.AuctSessJoin;
import com.ec.recauctionec.data.entities.AuctionSession;
import com.ec.recauctionec.data.entities.Supplier;
import com.ec.recauctionec.data.repositories.AuctSessJoinRepo;
import com.ec.recauctionec.services.AuctSessJoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
    public AuctSessJoin findBestPriceAuctionJoinByAuction(AuctionSession auctionSession) {
        return joinRepo.findFirstByAuctionSessionOrderByPriceAsc(auctionSession);
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

    @Override
    public List<AuctSessJoin> findAllBySupplierAndDate(Supplier suppliers, Date date) {
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
