package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.Wallet;
import com.ec.recauctionec.data.entities.WalletTransaction;
import com.ec.recauctionec.data.response.WalletObjQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface WalletTransactionRepo extends JpaRepository<WalletTransaction, Integer> {
    @Query("select log from WalletTransaction log " +
            "where date(log.createdDate) = ?1 and log.wallet.walletId =?2")
    List<WalletTransaction> findLogByDate(Date date, int walletId);

    @Query("select log from WalletTransaction log " +
            "where date(log.createdDate) >= ?1 and log.wallet.walletId =?2")
    List<WalletTransaction> findLogFromFilterDateToCurrent(Date date, int walletId);

    @Query("select log from WalletTransaction log where log.wallet.walletId=?1 order by log.createdDate desc")
    List<WalletTransaction> find5RecentLogByWallet(int walletId);

    WalletTransaction findTop1ByWalletOrderByCreatedDateDesc(Wallet wallet);

    @Query("select date(i.createdDate) as createDate,sum(i.value) as value,i.type as type " +
            "from WalletTransaction i " +
            "where i.wallet.walletId=?1 " +
            "and month (i.createdDate) = month(?2) " +
            "and year(i.createdDate) = year(?2) " +
            "group by date(i.createdDate),i.type")
    List<WalletObjQuery> statisticTransactionForLineChart(int walletId, Date dateFilter);

    @Query("select i.type as type ,sum(i.value) as value " +
            "from WalletTransaction i " +
            "where i.wallet.walletId=?1 " +
            "and month (i.createdDate) = month(?2) " +
            "and year(i.createdDate) = year(?2) " +
            "group by month(i.createdDate),i.type")
    List<WalletObjQuery> statisticTransactionForPieChart(int walletId, Date dateFilter);

    @Query("select i.type as type, sum(i.value) as value from WalletTransaction i " +
            "where month(i.createdDate)=?1 and year(i.createdDate)=?2 group by i.type")
    List<WalletObjQuery> statisticAllTransactionInMonth(int month, int year);

}
