package com.ec.recauctionec.repositories;

import com.ec.recauctionec.entities.Wallet;
import com.ec.recauctionec.entities.WalletHistory;
import com.ec.recauctionec.response.WalletObjQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface WalletHistoryRepo extends JpaRepository<WalletHistory, Integer> {
    @Query("select log from WalletHistory log " +
            "where date(log.createDate) = ?1 and log.wallet.walletId =?2")
    List<WalletHistory> findLogByDate(Date date, int walletId);

    @Query("select log from WalletHistory log " +
            "where date(log.createDate) >= ?1 and log.wallet.walletId =?2")
    List<WalletHistory> findLogFromFilterDateToCurrent(Date date, int walletId);

    WalletHistory findTop1ByWalletOrderByCreateDateDesc(Wallet wallet);

    @Query("select date(i.createDate) as createDate,sum(i.value) as value,i.type as type " +
            "from WalletHistory i " +
            "where i.wallet.walletId=?1 " +
            "and month (i.createDate) = month(?2) " +
            "and year(i.createDate) = year(?2) " +
            "group by date(i.createDate),i.type")
    List<WalletObjQuery> statisticTransactionForLineChart(int walletId, Date dateFilter);

    @Query("select i.type as type ,sum(i.value) as value " +
            "from WalletHistory i " +
            "where i.wallet.walletId=?1 " +
            "and month (i.createDate) = month(?2) " +
            "and year(i.createDate) = year(?2) " +
            "group by month(i.createDate),i.type")
    List<WalletObjQuery> statisticTransactionForPieChart(int walletId, Date dateFilter);

}
