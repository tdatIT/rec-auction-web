package com.ec.recauctionec.repositories;

import com.ec.recauctionec.entities.AuctSessJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuctSessJoinRepo extends JpaRepository<AuctSessJoin, Long> {
    @Query("select j from AuctSessJoin j where j.auctionSession.auctionSessId = ?1")
    List<AuctSessJoin> findAllByAuctionId(int auctionId);



}
