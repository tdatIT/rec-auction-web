package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.AuctionSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface AuctionRepo extends JpaRepository<AuctionSession, Integer> {

    @Query("from AuctionSession a where a.isComplete = false and a.user.userId = ?1")
    List<AuctionSession> findActiveAuction(int userId);

    @Query("select a from AuctionSession a " +
            "where a.isComplete = false " +
            "and date(a.endDate) = :date")
    List<AuctionSession> findAllActiveAuctionByDate(@Param("date") Date date);

    @Query("select a from AuctionSession a" +
            " where a.user.userId = :userId and (date(a.startDate)<=:date and date(a.endDate)>=:date)")
    List<AuctionSession> findAuctionOfUserAtTime(
            @Param("userId") int userId,
            @Param("date") Date date
    );


    @Query("select a from AuctionSession a where " +
            "date(a.createDate)<=:currentDay " +
            "and date(a.endDate)>=:currentDay " +
            "and a.isComplete=false " +
            "order by a.endDate asc")
    List<AuctionSession> findTop10AuctionForDay(Pageable top10,
                                                @Param("currentDay") java.util.Date current);

}
