package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WalletRepo extends JpaRepository<Wallet, Integer> {
    @Query("from Wallet w where w.user.userId = ?1")
    List<Wallet> findByUserId(int userId);

}