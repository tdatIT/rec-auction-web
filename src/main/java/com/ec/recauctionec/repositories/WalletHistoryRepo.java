package com.ec.recauctionec.repositories;

import com.ec.recauctionec.entities.Wallet;
import com.ec.recauctionec.entities.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface WalletHistoryRepo extends JpaRepository<WalletHistory, Integer> {
    @Query("select log from WalletHistory log " +
            "where date(log.createDate) = ?1 and log.wallet.walletId =?2")
    List<WalletHistory> findLogByDate(Date date, int walletId);


    WalletHistory findTop1ByWalletOrderByCreateDateDesc(Wallet wallet);

}
