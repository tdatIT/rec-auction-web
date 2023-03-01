package com.ec.recauctionec.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "delivery")
public class Delivery {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "delivery_id", nullable = false)
    private int deliveryId;

    
    @Column(name = "discount", nullable = false)
    private int discount;

    
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @OneToMany(mappedBy = "delivery")
    private Collection<Orders> orders;

    public Delivery(int deliveryId) {
        this.deliveryId = deliveryId;
    }
}
