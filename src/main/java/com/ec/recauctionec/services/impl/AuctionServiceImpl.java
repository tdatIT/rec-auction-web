package com.ec.recauctionec.services.impl;

import com.ec.recauctionec.dto.AuctionSessionDTO;
import com.ec.recauctionec.entities.*;
import com.ec.recauctionec.repositories.AuctSessJoinRepo;
import com.ec.recauctionec.repositories.AuctionRepo;
import com.ec.recauctionec.repositories.UserRepo;
import com.ec.recauctionec.repositories.WalletRepo;
import com.ec.recauctionec.services.AuctionService;
import com.ec.recauctionec.services.StorageImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuctionServiceImpl implements AuctionService {

    public final static double CHECK_AVAILABLE = 0.3D;
    public final static int CHECK_TOTAL_AUCTION = 5;

    @Autowired
    private AuctionRepo auctionRepo;
    @Autowired
    private UserRepo user;
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private StorageImage storageImage;
    @Autowired
    private AuctSessJoinRepo joinRepo;

    @Override
    public AuctionSession findById(int id) {
        return auctionRepo.findById(id).orElseThrow();
    }

    @Override
    public List<AuctionSession> findAllByDate(Date date) {
        return auctionRepo.findAllActiveAuctionByDate(date);
    }

    @Override
    public List<AuctionSession> findAllByUserAndActive(int userId, Date date) {
        return auctionRepo.findAuctionOfUserAtTime(userId, date);
    }

    @Override
    @Transactional
    public boolean createNewAuction(User us, AuctionSessionDTO dto) {
        boolean status = false;
        try {
            AuctionSession auction = dto.mapping();
            Wallet w_us = walletRepo.findByUserId(us.getUserId()).get(0);
            if (w_us.getAccountBalance() >= (dto.getReservePrice() * CHECK_AVAILABLE)
                    && auctionRepo.findActiveAuction(us.getUserId()).size() < CHECK_TOTAL_AUCTION) {
                auction.setUser(us);
                if (dto.isAuto()) {
                    //business logic process
                }
                if (dto.getImg() != null) {
                    List<AuctionImg> imgs = new ArrayList<>();
                    List<String> filenames = storageImage.storageMultiImage(dto.getImg());
                    for (String name : filenames) {
                        AuctionImg img = new AuctionImg();
                        img.setImageFile(name);
                        img.setAuction(auction);
                        imgs.add(img);
                    }
                    auction.setImg(imgs);
                }
                auctionRepo.save(auction);
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
        AuctionSession auction = auctionRepo.findById(auctionId).orElseThrow();
        if (auction != null) {

            if (auction.getAuctSessJoins().size() > 0) {
                for (AuctSessJoin join : auction.getAuctSessJoins()) {
                    join.setStatus(AuctSessJoin.LOSS);
                    joinRepo.save(join);
                }
                AuctSessJoin winner = joinRepo.findFirstByAuctionSessionOrderByPriceAsc(auction);
                winner.setStatus(AuctSessJoin.WIN);
                auction.setComplete(true);
                //save into db
                auctionRepo.save(auction);
                joinRepo.save(winner);
            }
        }
    }

    @Override
    @Transactional
    public boolean cancelAuction(int auctionId) {
        AuctionSession auction = auctionRepo.findById(auctionId).orElseThrow();
        if (auction != null) {
            for (AuctSessJoin join : auction.getAuctSessJoins()) {
                join.setStatus(AuctSessJoin.LOSS);
                joinRepo.save(join);
            }
            auction.setComplete(true);
            auctionRepo.save(auction);
            return true;
        }
        return false;
    }

    @Override
    public List<AuctionSession> findTop10AuctionForDay() {
        Pageable top10 = PageRequest.of(0, 10);
        return auctionRepo.findTop10AuctionForDay(top10, new java.util.Date(new java.util.Date().getTime()));
    }

}
