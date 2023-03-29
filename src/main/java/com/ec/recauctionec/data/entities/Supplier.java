package com.ec.recauctionec.data.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.List;

@Setter
@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "supplier")
public class Supplier {
    public static final int NUM_OF_PRODUCTS_FOR_BASIC = 5;
    public static final int NUM_OF_PRODUCTS_FOR_PREMIUM = 20;
    public static final int NUM_OF_PRODUCTS_FOR_MALL = 40;
    public static final int LEVEL_BASIC = 1;
    public static final int LEVEL_PREMIUM = 2;
    public static final int LEVEL_MALL = 3;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "supplier_id", nullable = false)
    private int supplierId;

    @Column(name = "createDate", nullable = false)
    private Date createDate;

    @Column(name = "isActive", nullable = false)
    private boolean isActive;

    @Column(name = "level_supp", nullable = false)
    private int levelSupp;

    @Column(name = "rating", nullable = false)
    private int rating;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.EAGER)
    private Collection<Product> products;

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id")
    private User user;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AddressData> addresses;


}
