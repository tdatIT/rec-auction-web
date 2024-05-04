package com.ec.recauctionec.services.impl;

import com.ec.recauctionec.data.dto.AuctionSessionDTO;
import com.ec.recauctionec.data.entities.*;
import com.ec.recauctionec.data.repositories.BidParticipantRepos;
import com.ec.recauctionec.data.repositories.BidRepos;
import com.ec.recauctionec.data.repositories.UserRepo;
import com.ec.recauctionec.data.repositories.WalletRepo;
import com.ec.recauctionec.services.BidService;
import com.ec.recauctionec.services.StorageImage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    public final static double CHECK_AVAILABLE = 0.3D;
    public final static int CHECK_TOTAL_AUCTION = 5;
    private final BidRepos bidRepos;
    private final WalletRepo walletRepo;
    private final StorageImage storageImage;
    private final BidParticipantRepos joinRepo;

    @Override
    public List<Bid> findAllByDateAndPageSize(int page, int size, Date dateFilter) {
        return bidRepos.findByDateAndPageSize(PageRequest.of(page, size), dateFilter);
    }

    @Override
    public Bid findById(int id) {
        return bidRepos.findById(id).orElseThrow();
    }

    @Override
    public List<Bid> find5LastBidByUserId(User user) {
        return bidRepos.find5LastBidByUserId(user.getUserId(), PageRequest.of(0, 5));
    }

    @Override
    public List<Bid> findAllByDate(Date date) {
        return bidRepos.findAllActiveAuctionByDate(date);
    }

    @Override
    public List<Bid> findAllByUserAndActive(int userId, Date date) {
        return bidRepos.findAuctionOfUserAtTime(userId, date);
    }

    @Override
    @Transactional
    public boolean createNewAuction(User us, AuctionSessionDTO dto) {
        boolean status = false;
        try {
            Bid auction = dto.mapping();
            Wallet w_us = walletRepo.findByUserId(us.getUserId()).get(0);
            if (w_us.getAccountBalance() >= (dto.getReservePrice() * CHECK_AVAILABLE)
                    && bidRepos.findActiveAuction(us.getUserId()).size() < CHECK_TOTAL_AUCTION) {
                auction.setUser(us);
                if (dto.isAuto()) {
                    //business logic process
                }
                if (dto.getImg() != null) {
                    List<BidImg> imgs = new ArrayList<>();
                    List<String> filenames = storageImage.storageMultiImage(dto.getImg());
                    for (String name : filenames) {
                        BidImg img = new BidImg();
                        img.setImageFile(name);
                        img.setAuction(auction);
                        imgs.add(img);
                    }
                    auction.setImg(imgs);
                }
                bidRepos.save(auction);
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override
    @Transactional
    public void setWinAuctionSession(int auctionId) {
        Bid auction = bidRepos.findById(auctionId).orElseThrow();
        if (auction != null) {

            if (auction.getBidderData().size() > 0) {
                for (BidParticipant join : auction.getBidderData()) {
                    join.setStatus(BidParticipant.LOSS);
                    joinRepo.save(join);
                }
                BidParticipant winner = joinRepo.findFirstByBidOrderByPriceAsc(auction);
                winner.setStatus(BidParticipant.WIN);
                auction.setComplete(true);
                //save into db
                bidRepos.save(auction);
                joinRepo.save(winner);
            }
        }
    }

    @Override
    @Transactional
    public boolean cancelAuction(int auctionId) {
        Bid auction = bidRepos.findById(auctionId).orElseThrow();
        if (auction != null) {
            for (BidParticipant join : auction.getBidderData()) {
                join.setStatus(BidParticipant.LOSS);
                joinRepo.save(join);
            }
            auction.setComplete(true);
            bidRepos.save(auction);
            return true;
        }
        return false;
    }

    @Override
    public List<Bid> findTop10AuctionForDay() {
        Pageable top10 = PageRequest.of(0, 10);
        return bidRepos.findTop10AuctionForDay(top10, new java.util.Date(new java.util.Date().getTime()));
    }

}
