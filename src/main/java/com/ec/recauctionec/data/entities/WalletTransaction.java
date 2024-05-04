package com.ec.recauctionec.data.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "wallet_transaction")
public class WalletTransaction {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "transaction_id", nullable = false)
    private int transId;
    
    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;
    
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
