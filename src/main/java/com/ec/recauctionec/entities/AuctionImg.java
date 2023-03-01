package com.ec.recauctionec.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "auction_img")
public class AuctionImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    @Column(name = "image_file")
    private String imageFile;

    @ManyToOne
    @JoinColumn(name = "auction_session_id")
    private AuctionSession auction;

}
