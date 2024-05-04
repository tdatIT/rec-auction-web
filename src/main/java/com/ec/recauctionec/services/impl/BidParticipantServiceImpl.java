package com.ec.recauctionec.services.impl;

import com.ec.recauctionec.data.entities.BidParticipant;
import com.ec.recauctionec.data.entities.Bid;
import com.ec.recauctionec.data.entities.Supplier;
import com.ec.recauctionec.data.repositories.BidParticipantRepos;
import com.ec.recauctionec.services.BidParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BidParticipantServiceImpl implements BidParticipantService {
    private final BidParticipantRepos joinRepo;

    @Override
    public List<BidParticipant> findAllByAuctionId(int auctionId) {
        return joinRepo.findAllByAuctionId(auctionId);
    }

    @Override
    public BidParticipant findBestPriceAuctionJoinByAuction(Bid bid) {
        return joinRepo.findFirstByBidOrderByPriceAsc(bid);
    }

    @Override
    public boolean joinAuction(BidParticipant join) {
        joinRepo.save(join);
        return true;
    }

    @Override
    public boolean updateJoin(BidParticipant join) {
        joinRepo.save(join);
        return true;
    }

    @Override
    public BidParticipant findById(long id) {
        return joinRepo.findById(id).orElseThrow();
    }

    @Override
    public List<BidParticipant> findAllBySupplierAndDate(Supplier suppliers, Date date) {
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
