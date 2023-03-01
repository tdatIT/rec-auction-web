package com.ec.recauctionec.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "wallet_history")
public class WalletHistory {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "no_history", nullable = false)
    private int noHistory;
    
    @Column(name = "createDate", nullable = false)
    private Timestamp createDate;
    
    @Column(name = "type", nullable = false)
    private boolean type;
    
    @Column(name = "value", nullable = false, precision = 0)
    private double value;

    
    @Column(name = "payment_id")
    private String paymentId;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
}
