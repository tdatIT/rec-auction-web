package com.ec.recauctionec.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "product_tag")
public class ProductTag {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ptag_id", nullable = false)
    private long ptagId;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "detail", nullable = false, length = 255)
    private String detail;

}
