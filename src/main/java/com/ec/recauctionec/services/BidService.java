package com.ec.recauctionec.services;

import com.ec.recauctionec.data.dto.AuctionSessionDTO;
import com.ec.recauctionec.data.entities.Bid;
import com.ec.recauctionec.data.entities.User;

import java.sql.Date;
import java.util.List;

public interface BidService {

    Bid findById(int id);

    List<Bid> findAllByDate(Date date);

    List<Bid> findTop10AuctionForDay();

    List<Bid> findAllByUserAndActive(int userId, Date dateFilter);

    List<Bid> findAllByDateAndPageSize(int page, int size, Date dateFilter);

    List<Bid> find5LastBidByUserId(User user);


    boolean createNewAuction(User us, AuctionSessionDTO dto);

    void setWinAuctionSession(int auctionId);

    boolean cancelAuction(int auctionId);


}
