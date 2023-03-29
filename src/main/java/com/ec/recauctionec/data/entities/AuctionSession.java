package com.ec.recauctionec.data.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "auction_session")
public class AuctionSession {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "auction_sess_id", nullable = false)
    private int auctionSessId;


    @Column(name = "closing_price", nullable = true, precision = 0)
    private Double closingPrice;


    @Column(name = "createDate", nullable = false)
    private Timestamp createDate;


    @Column(name = "end_date", nullable = false)
    private Timestamp endDate;

    @Column(name = "isComplete", nullable = false)
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

    @OneToMany(mappedBy = "auctionSession", fetch = FetchType.LAZY)
    private Collection<AuctSessJoin> auctSessJoins;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "auction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<AuctionImg> img;


}
