package com.ec.recauctionec.data.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "commission")
public class Commission {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "amount_from_buyer", nullable = false, precision = 0)
    private double amountFromBuyer;

    @Column(name = "amount_from_supplier", nullable = false, precision = 0)
    private double amountFromSupplier;

    @OneToOne(mappedBy = "commission")
    private Orders order;


}
