package com.ec.recauctionec.services;

import com.ec.recauctionec.data.dto.AuctionSessionDTO;
import com.ec.recauctionec.data.entities.AuctionSession;
import com.ec.recauctionec.data.entities.User;

import java.sql.Date;
import java.util.List;

public interface AuctionService {

    AuctionSession findById(int id);

    List<AuctionSession> findAllByDate(Date date);

    List<AuctionSession> findTop10AuctionForDay();

    List<AuctionSession> findAllByUserAndActive(int userId, Date dateFilter);

    List<AuctionSession> findAllByDateAndPageSize(int page, int size, Date dateFilter);


    boolean createNewAuction(User us, AuctionSessionDTO dto);

    void setWinAuctionSession(int auctionId);

    boolean cancelAuction(int auctionId);


}
