package com.ec.recauctionec.data.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bid_img")
public class BidImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "image_file")
    private String imageFile;

    @ManyToOne
    @JoinColumn(name = "bid_id")
    private Bid auction;

}
