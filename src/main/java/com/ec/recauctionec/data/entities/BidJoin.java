package com.ec.recauctionec.data.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@EqualsAndHashCode

@Table(name = "bid_joins")
public class BidJoin {

    public static final int NOT_CONFIRM = 1;
    public static final int ACTIVE = 2;
    public static final int LOSS = 3;
    public static final int WIN = 4;
    public static final int CANCEL = 0;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "price", nullable = false, precision = 0)
    private double price;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "time", nullable = false)
    private Timestamp time;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "bid_id")
    private Bid bid;

}
