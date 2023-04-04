package com.ec.recauctionec.data.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "category")
public class Category {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "category_id", nullable = false)
    private int categoryId;
    
    @Column(name = "category_name", nullable = false, length = 255)
    private String categoryName;
    
    @Column(name = "detail", nullable = true, length = 255)
    private String detail;
    
    @Column(name = "image", nullable = true, length = 255)
    private String image;

    @OneToMany(mappedBy = "category")
    private Collection<Bid> bids;

    @OneToMany(mappedBy = "category")
    private Collection<Product> products;


}
