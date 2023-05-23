package com.ec.recauctionec.data.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "orders")
public class Orders {
    public static final int NOT_CONFIRM = 1;
    public static final int CONFIRM = 2;
    public static final int DELIVERY = 3;
    public static final int COMPLETE = 4;
    public static final int CANCEL = 0;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "createDate", nullable = false)
    private Timestamp createDate;

    @Column(name = "updateDate", nullable = false)
    private Date updateDate;

    @Column(name = "shipping_price", nullable = false, precision = 0)
    private double shippingPrice;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "total_price", nullable = false, precision = 0)
    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressData address;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "commission_id", referencedColumnName = "order_id")
    private Commission commission;
}
