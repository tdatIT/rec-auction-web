package com.ec.recauctionec.data.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "role")
public class Role {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "role_id", nullable = false)
    private int roleId;

    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Collection<User> users;

    public Role(int roleId) {
        this.roleId = roleId;
    }

    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_SUPPLIER = 2;
    public static final int ROLE_USER = 3;
    public static final int ROLE_GUEST = 4;


}
