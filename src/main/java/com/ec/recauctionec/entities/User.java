package com.ec.recauctionec.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "user")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id", nullable = false)
    private int userId;


    @Column(name = "avatar", nullable = true, length = 255)
    private String avatar;


    @Column(name = "createDate", nullable = false)
    private Date createDate;


    @Column(name = "email", nullable = false, length = 50)
    private String email;


    @Column(name = "firstName", nullable = false, length = 50)
    private String firstName;


    @Column(name = "isActive", nullable = false)
    private boolean isActive;


    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;


    @Column(name = "level_user", nullable = false)
    private int levelUser;


    @Column(name = "password", nullable = false, length = 255)
    private String password;


    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;


    @Column(name = "username", nullable = true, length = 255)
    private String username;

    @OneToMany(mappedBy = "user")
    private Collection<AuctionSession> auctionSessions;

    @OneToOne(mappedBy = "user")
    private Supplier suppliers;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<AddressData> userAddresses;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Wallet wallet;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Orders> orders;

}
