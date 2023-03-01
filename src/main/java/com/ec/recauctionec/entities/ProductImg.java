package com.ec.recauctionec.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "product_img")
public class ProductImg {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "img_id", nullable = false)
    private int imgId;
    
    @Column(name = "img_name", nullable = false)
    private String imgName;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
