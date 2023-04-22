package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.Bid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface BidRepos extends JpaRepository<Bid, Integer> {

    @Query("from Bid a where a.isComplete = false and a.user.userId = ?1")
    List<Bid> findActiveAuction(int userId);

    @Query("select a from Bid a " +
            "where a.isComplete = false " +
            "and date(a.endDate) = :date")
    List<Bid> findAllActiveAuctionByDate(@Param("date") Date date);

    @Query("select a from Bid a" +
            " where a.user.userId = :userId and (date(a.startDate)<=:date and date(a.endDate)>=:date)")
    List<Bid> findAuctionOfUserAtTime(
            @Param("userId") int userId,
            @Param("date") Date date
    );


    @Query("select a from Bid a where " +
            "date(a.createDate)<=:currentDay " +
            "and date(a.endDate)>=:currentDay " +
            "and a.isComplete=false " +
            "order by a.endDate asc")
    List<Bid> findTop10AuctionForDay(Pageable top10,
                                     @Param("currentDay") java.util.Date current);

    @Query("select a from Bid a where date(a.startDate) > ?1")
    List<Bid> findByDateAndPageSize(Pageable pageable, Date filterDate);

    @Query("select a from Bid a where a.user.userId=?1 order by a.createDate desc")
    List<Bid> find5LastBidByUserId(int userId, Pageable pageable);
}
