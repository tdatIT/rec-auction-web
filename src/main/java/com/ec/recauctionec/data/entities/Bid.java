package com.ec.recauctionec.data.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "bids")
public class Bid {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "bid_id", nullable = false)
    private int bidId;


    @Column(name = "closing_price", nullable = true, precision = 0)
    private Double closingPrice;


    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;


    @Column(name = "ended_date", nullable = false)
    private Timestamp endedDate;

    @Column(name = "is_complete", nullable = false)
    private boolean isComplete;

    @Column(name = "product_key", nullable = false, length = 255)
    private String productKey;

    @Column(name = "reserve_price", nullable = false, precision = 0)
    private double reservePrice;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "product_tag_str", nullable = true, length = 255)
    private String productTagStr;

    @OneToMany(mappedBy = "bid", fetch = FetchType.LAZY)
    private Collection<BidParticipant> bidderData;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "auction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<BidImg> img;


}
